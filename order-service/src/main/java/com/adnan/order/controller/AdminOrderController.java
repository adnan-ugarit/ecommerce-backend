package com.adnan.order.controller;

import com.adnan.order.entity.Order;
import com.adnan.order.exception.AuthenticationFailException;
import com.adnan.order.model.User;
import com.adnan.order.service.OrderService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/admin/orders")
public class AdminOrderController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminOrderController.class);
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    RestTemplate restTemplate;
    
    private static final String AUTHENTICATION_URL = "http://auth-service/auth/authenticate";
    
    @GetMapping("")
    public ResponseEntity<List<Order>> getAllOrders(HttpServletRequest request) {
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
        if (!(("MARKETING_MANAGER".equals(user.getRole())) || ("SALES_MANAGER".equals(user.getRole())) || ("ADMIN".equals(user.getRole())))) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        LOGGER.info("Finding all orders");
        List<Order> body = orderService.getAllOrders();
        return new ResponseEntity<>(body, HttpStatus.OK);
    }
    
    /*@GetMapping("/{orderId}")
    public ResponseEntity<Order> findOrderById(@PathVariable("orderId") Long orderId) {
        LOGGER.info("Finding order by id: {}", orderId);
        Order order = orderService.findOrderById(orderId);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }*/
    
    @PutMapping("/update/{orderId}")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable("orderId") Long orderId,
            HttpServletRequest request) {
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
        if (!(("SALES_MANAGER".equals(user.getRole())) || ("ADMIN".equals(user.getRole())))) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        LOGGER.info("Finding order by orderId: {}", orderId);
        Order order = orderService.findOrderById(orderId);
        if (order == null) {
            LOGGER.error("Order (orderId = {}) is not found", orderId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (order.getStatus() != Order.Status.Approved) {
            LOGGER.error("Order (orderId = {}) is not found", orderId);
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        order.setStatus(Order.Status.Delivered);
        LOGGER.info("Updating order ({}) status to Delivered", order);
        orderService.saveOrder(order);
        return new ResponseEntity<>(order, HttpStatus.OK);
        
    }
    
}
