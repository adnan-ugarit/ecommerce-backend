package com.adnan.auth.service;

import com.adnan.auth.client.UserClient;
import com.adnan.auth.model.User;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserClientService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(UserClientService.class);
    
    @Autowired
    private UserClient userClient;
    
    @HystrixCommand(fallbackMethod = "getDefaultAuthUserByEmail")
    public User getAuthUserByEmail(String userEmail) {
        return userClient.fetchUserByEmail(userEmail);
    }
    
    User getDefaultAuthUserByEmail(String userEmail) {
        LOGGER.info("Returning default user for userEmail: {}", userEmail);
        User user = new User();
        user.setId(0L);
        user.setEmail(userEmail);
        user.setUsername("anonymous");
        user.setPassword("");
        return user;
    }
    
    @HystrixCommand(fallbackMethod = "getDefaultAuthUserByUsername")
    public User getAuthUserByUsername(String username) {
        return userClient.fetchUserByUsername(username);
    }
    
    User getDefaultAuthUserByUsername(String username) {
        LOGGER.info("Returning default user for username: {}", username);
        User user = new User();
        user.setId(0L);
        user.setEmail("anonymous@gmail.com");
        user.setUsername(username);
        user.setPassword("");
        return user;
    }
    
}
