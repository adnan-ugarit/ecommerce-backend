package com.adnan.user.service;

import com.adnan.user.dto.SignUpDto;
import com.adnan.user.entity.User;
import com.adnan.user.entity.User.Role;
import com.adnan.user.exception.CustomException;
import com.adnan.user.repository.UserRepository;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import javax.xml.bind.DatatypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    
    @Autowired
    UserRepository userRepository;
    
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    public Optional<User> findUserById(Long userId) {
        return userRepository.findById(userId);
    }
    
    public Optional<User> findUserByEmail(String userEmail) {
        return userRepository.findByEmail(userEmail);
    }
    
    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    public void signUp(SignUpDto signUpDto) throws CustomException {
        if (userRepository.findByEmail(signUpDto.getEmail()).isPresent()) {
            throw new CustomException("email already exists");
        }
        if (userRepository.findByUsername(signUpDto.getUsername()).isPresent()) {
            throw new CustomException("username already exists");
        }
        String encryptedPassword = "";
        try {
            encryptedPassword = hashPassword(signUpDto.getPassword());
        }
        catch (NoSuchAlgorithmException e) {
            LOGGER.error("Hashing password failed, {}", e.getMessage());
        }
        User user = new User(signUpDto.getEmail(), signUpDto.getUsername(), encryptedPassword,
                signUpDto.getFirstName(), signUpDto.getLastName(), Role.USER);
        userRepository.save(user);
    }
    
    public void updateUserInformation(User userResponse, User user) {
        if ((userRepository.findByEmail(user.getEmail()).isPresent()) && (!user.getEmail().equals(userResponse.getEmail()))) {
            throw new CustomException("email already exists");
        }
        if ((userRepository.findByUsername(user.getUsername()).isPresent()) && (!user.getUsername().equals(userResponse.getUsername()))) {
            throw new CustomException("username already exists");
        }
        user.setId(userResponse.getId());
        String encryptedPassword = "";
        try {
            encryptedPassword = hashPassword(user.getPassword());
        }
        catch (NoSuchAlgorithmException e) {
            LOGGER.error("Hashing password failed, {}", e.getMessage());
        }
        user.setPassword(encryptedPassword);
        user.setRole(userResponse.getRole());
        userRepository.save(user);
    }
    
    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte[] digest = md.digest();
        String myHash = DatatypeConverter
                .printHexBinary(digest).toUpperCase();
        return myHash;
    }
    
    public void updateUserRole(User user, Role role) {        
        user.setRole(role);
        userRepository.save(user);
    }
    
}
