package com.adnan.auth.client;

import com.adnan.auth.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("user-service")
public interface UserClient {
    
    @GetMapping("/user/email/{userEmail}")
    public User fetchUserByEmail(@PathVariable("userEmail") String userEmail);
    
    @GetMapping("/user/username/{username}")
    public User fetchUserByUsername(@PathVariable("username") String username);
    
}
