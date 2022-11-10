package com.adnan.order.service;

import com.adnan.order.client.CartClient;
import com.adnan.order.model.CartItem;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartClientService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CartClientService.class);
    
    @Autowired
    private CartClient cartClient;
    
    @HystrixCommand(fallbackMethod = "getDefaultOrderCartItems")
    public List<CartItem> getOrderCartItems(String cartId) {
        return cartClient.fetchCartItems(cartId);
    }
    
    List<CartItem> getDefaultOrderCartItems(String cartId) {
        LOGGER.info("Returning default cart items for cartId: {}", cartId);
        return new ArrayList<>();
    }
    
}
