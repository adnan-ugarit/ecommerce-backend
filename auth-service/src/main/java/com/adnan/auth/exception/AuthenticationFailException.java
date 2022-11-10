package com.adnan.auth.exception;

public class AuthenticationFailException extends IllegalArgumentException {
    
    public AuthenticationFailException(String msg) {
        super(msg);
    }
    
}
