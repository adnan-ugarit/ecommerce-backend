package com.adnan.user.controller;

import com.adnan.user.common.ApiResponse;
import com.adnan.user.dto.SignUpDto;
import com.adnan.user.entity.User;
import com.adnan.user.exception.AuthenticationFailException;
import com.adnan.user.exception.CustomException;
import com.adnan.user.service.UserService;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/user")
public class UserController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    
    @Autowired
    private UserService userService;
    
    @Autowired
    RestTemplate restTemplate;
    
    private static final String AUTHENTICATION_URL = "http://auth-service/auth/authenticate";
    
    @GetMapping("/email/{userEmail}")
    public ResponseEntity<User> findUserByEmail(@PathVariable("userEmail") String userEmail) {
        LOGGER.info("Finding user by email: {}", userEmail);
        Optional<User> optionalUser = userService.findUserByEmail(userEmail);
        if (!optionalUser.isPresent()) {
            LOGGER.error("User (email = {}) is not found", userEmail);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        User user = optionalUser.get();
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    
    @GetMapping("/username/{username}")
    public ResponseEntity<User> findUserByUsername(@PathVariable("username") String username) {
        LOGGER.info("Finding user by username: {}", username);
        Optional<User> optionalUser = userService.findUserByUsername(username);
        if (!optionalUser.isPresent()) {
            LOGGER.error("User (username = {}) is not found", username);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        User user = optionalUser.get();
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    
    @PostMapping("/create")
    public ResponseEntity<ApiResponse> signUp(@RequestBody SignUpDto signUpDto) throws CustomException {
        LOGGER.info("Creating user: {}", signUpDto);
        userService.signUp(signUpDto);
        return new ResponseEntity<>(new ApiResponse(true, "User has been created successfully"), HttpStatus.CREATED);
    }
    
    @PutMapping("/update")
    public ResponseEntity<ApiResponse> updateUserInformation(@RequestBody User user, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || "".equals(token)) {
            return new ResponseEntity<>(new ApiResponse(false, "Token not present"), HttpStatus.UNAUTHORIZED);
        }
        User userResponse;
        try {
            userResponse = restTemplate.postForObject(AUTHENTICATION_URL, token, User.class);
        }
        catch (AuthenticationFailException ex) {
            return new ResponseEntity<>(new ApiResponse(false, ex.getMessage()), HttpStatus.UNAUTHORIZED);
        }
        catch (Exception ex) {
            LOGGER.error("", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        LOGGER.info("Updating user information: {}", user);
        userService.updateUserInformation(userResponse, user);
        return new ResponseEntity<>(new ApiResponse(true, "User information has been updated"), HttpStatus.OK);
    }
    
}
