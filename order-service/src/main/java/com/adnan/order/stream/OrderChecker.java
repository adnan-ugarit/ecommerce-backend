package com.adnan.order.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface OrderChecker {
    
    String ORDER_OUT = "orderChannel";
    String RESULT_IN = "resultChannel";

    @Input(RESULT_IN)
    SubscribableChannel result();

    @Output(ORDER_OUT)
    MessageChannel order();
    
}
