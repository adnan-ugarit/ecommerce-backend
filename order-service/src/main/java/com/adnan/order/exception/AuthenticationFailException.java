package com.adnan.order.exception;

public class AuthenticationFailException extends IllegalArgumentException {
    
    public AuthenticationFailException(String msg) {
        super(msg);
    }
    
}
