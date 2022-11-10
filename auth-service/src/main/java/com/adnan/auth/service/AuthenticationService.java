package com.adnan.auth.service;

import com.adnan.auth.dto.SignInDto;
import com.adnan.auth.exception.AuthenticationFailException;
import com.adnan.auth.model.User;
import com.adnan.auth.util.JwtTokenUtil;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.xml.bind.DatatypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);
    
    @Autowired
    UserClientService userClientService;
    
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    
    public String signIn(SignInDto signInDto) throws AuthenticationFailException {
        LOGGER.info("Fetching user by email: {}", signInDto.getEmail());
        User user = userClientService.getAuthUserByEmail(signInDto.getEmail());
        if(user == null) {
            throw new AuthenticationFailException("User not present");
        }
        try {
            if (!user.getPassword().equals(hashPassword(signInDto.getPassword()))) {
                throw new AuthenticationFailException("Password is not correct");
            }
        }
        catch (NoSuchAlgorithmException e) {
            LOGGER.error("Hashing password failed, {}", e.getMessage());
        }
        LOGGER.info("Generating token for user: {}", user);
        return jwtTokenUtil.generateToken(user.getUsername(), user.getRole());
    }
    
    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte[] digest = md.digest();
        String myHash = DatatypeConverter
                .printHexBinary(digest).toUpperCase();
        return myHash;
    }
    
    private User getUser(String token) {
        String username;
        try {
            username = jwtTokenUtil.getUsername(token);
        }
        catch (Exception ex) {
            LOGGER.warn("Unable to parse JWT Token: {}", token, ex);
            return null;
        }
        if (username != null && !jwtTokenUtil.isTokenExpired(token)) {
            LOGGER.info("Fetching user by username: {}", username);
            User user = userClientService.getAuthUserByUsername(username);
            return user;
        }
        return null;
    }
    
    public User authenticate(String token) throws AuthenticationFailException {
        if (token == null || "".equals(token)) {
            throw new AuthenticationFailException("Token not present");
        }
        User user = getUser(token);
        if (user == null) {
            throw new AuthenticationFailException("Token not valid");
        }
        if (user.getId() == 0) {
            LOGGER.error("Unable fetch user from user service");
            throw new AuthenticationFailException("Sorry, Try again");
        }
        return user;
    }
    
}
