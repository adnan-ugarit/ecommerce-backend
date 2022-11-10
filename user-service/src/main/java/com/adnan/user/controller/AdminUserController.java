package com.adnan.user.controller;

import com.adnan.user.common.ApiResponse;
import com.adnan.user.entity.User;
import com.adnan.user.exception.AuthenticationFailException;
import com.adnan.user.service.UserService;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/admin/user")
public class AdminUserController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminUserController.class);
    
    @Autowired
    private UserService userService;
    
    @Autowired
    RestTemplate restTemplate;
    
    private static final String AUTHENTICATION_URL = "http://auth-service/auth/authenticate";
    
    @GetMapping("all")
    public ResponseEntity<List<User>> getAllUsers(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || "".equals(token)) {
            throw new RuntimeException("Token not present");
        }
        User user;
        try {
            user = restTemplate.postForObject(AUTHENTICATION_URL, token, User.class);
        }
        catch (AuthenticationFailException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        catch (Exception ex) {
            LOGGER.error("", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (!"ADMIN".equals(user.getRole().toString())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        LOGGER.info("Finding all users");
        List<User> body = userService.getAllUsers();
        return new ResponseEntity<>(body, HttpStatus.OK);
    }
    
    @PutMapping(value = "/update", params = {"userId", "role"})
    public ResponseEntity<ApiResponse> updateUserRole(HttpServletRequest request,
            @RequestParam("userId") Long userId, @RequestParam("role") User.Role role) {
        String token = request.getHeader("Authorization");
        if (token == null || "".equals(token)) {
            return new ResponseEntity<>(new ApiResponse(false, "Token not present"), HttpStatus.UNAUTHORIZED);
        }
        User user;
        try {
            user = restTemplate.postForObject(AUTHENTICATION_URL, token, User.class);
        }
        catch (AuthenticationFailException ex) {
            return new ResponseEntity<>(new ApiResponse(false, ex.getMessage()), HttpStatus.UNAUTHORIZED);
        }
        catch (Exception ex) {
            LOGGER.error("", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (!"ADMIN".equals(user.getRole().toString())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        LOGGER.info("Finding user by id: {}", userId);
        Optional<User> optionalUser = userService.findUserById(userId);
        if (!optionalUser.isPresent()) {
            LOGGER.error("User (id = {}) is not found", userId);
            return new ResponseEntity<>(new ApiResponse(false, "User is not found"), HttpStatus.NOT_FOUND);
        }
        user = optionalUser.get();
        LOGGER.info("Updating user (id = {}) role to {}", userId, role.toString());
        userService.updateUserRole(user, role);
        return new ResponseEntity<>(new ApiResponse(true, "User role has been updated"), HttpStatus.OK);
    }
    
}
