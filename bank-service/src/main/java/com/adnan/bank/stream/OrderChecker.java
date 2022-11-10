package com.adnan.bank.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface OrderChecker {
    
    String ORDER_IN = "orderChannel";
    String RESULT_OUT = "resultChannel";

    @Input(ORDER_IN)
    SubscribableChannel order();

    @Output(RESULT_OUT)
    MessageChannel result();
    
}
