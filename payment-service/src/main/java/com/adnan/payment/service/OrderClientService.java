package com.adnan.payment.service;

import com.adnan.payment.client.OrderClient;
import com.adnan.payment.model.CheckoutItem;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderClientService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderClientService.class);
    
    @Autowired
    private OrderClient orderClient;
    
    @HystrixCommand(fallbackMethod = "getDefaultPaymentOrderItemsByTrackingId")
    public List<CheckoutItem> getPaymentOrderItemsByTrackingId(UUID trackingId) {
        return orderClient.fetchOrderItemsByTrackingId(trackingId);
    }
    
    List<CheckoutItem> getDefaultPaymentOrderItemsByTrackingId(UUID trackingId) {
        LOGGER.info("Returning default order items for trackingId: {}", trackingId);
        return new ArrayList<>();
    }
    
}
