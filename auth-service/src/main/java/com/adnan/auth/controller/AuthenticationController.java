package com.adnan.auth.controller;

import com.adnan.auth.dto.SignInDto;
import com.adnan.auth.exception.AuthenticationFailException;
import com.adnan.auth.model.TokenResponse;
import com.adnan.auth.model.User;
import com.adnan.auth.service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class);
    
    @Autowired
    AuthenticationService authenticationService;
    
    @PostMapping("/login")
    public TokenResponse signIn(@RequestBody SignInDto signInDto) throws AuthenticationFailException {
        TokenResponse token = new TokenResponse(authenticationService.signIn(signInDto));
        LOGGER.info("Token is: {}", token);
        return token;
    }
    
    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> authenticateToken(@RequestBody String token) throws AuthenticationFailException {
        User user = authenticationService.authenticate(token);
        return ResponseEntity.ok(user);
    }
    
}
