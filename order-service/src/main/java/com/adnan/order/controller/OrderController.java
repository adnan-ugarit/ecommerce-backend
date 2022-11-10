package com.adnan.order.controller;

import com.adnan.order.common.ApiResponse;
import com.adnan.order.model.Item;
import com.adnan.order.entity.Order;
import com.adnan.order.entity.OrderItem;
import com.adnan.order.exception.AuthenticationFailException;
import com.adnan.order.model.CartItem;
import com.adnan.order.model.Product;
import com.adnan.order.model.User;
import com.adnan.order.service.CartClientService;
import com.adnan.order.service.OrderService;
import com.adnan.order.service.ProductClientService;
import com.adnan.order.stream.OrderChecker;
import com.adnan.order.stream.OrderStream;
import com.adnan.order.util.OrderItemUtility;
import com.adnan.order.util.OrderUtility;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/orders")
public class OrderController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private OrderChecker orderChecker;
    
    @Autowired
    private CartClientService cartClientService;
    
    @Autowired
    private ProductClientService productClientService;
    
    @Autowired
    RestTemplate restTemplate;
    
    private static final String AUTHENTICATION_URL = "http://auth-service/auth/authenticate";
    
    @GetMapping("/{trackingId}")
    public ResponseEntity<List<OrderItem>> findOrderItemsByTrackingId(@PathVariable("trackingId") UUID trackingId) {
        LOGGER.info("Finding order items by trackingId: {}", trackingId);
        Order order = orderService.findOrderByTrackingId(trackingId);
        if (order == null) {
            LOGGER.info("Order (trackingId = {}) is not found", trackingId);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        List<OrderItem> body = order.getItems();
        return new ResponseEntity<>(body, HttpStatus.OK);
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> findOrdersByUserId(@PathVariable("userId") Long userId,
            HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || "".equals(token)) {
            throw new RuntimeException("Token not present");
        }
        User user;
        try {
            user = restTemplate.postForObject(AUTHENTICATION_URL, token, User.class);
        }
        catch (AuthenticationFailException a) {
            return new ResponseEntity<>(new ArrayList(), HttpStatus.FORBIDDEN);
        }
        catch (Exception ex) {
            LOGGER.error("", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (user.getId() != userId) {
            LOGGER.error("User ({}), try to get other user's orders", user);
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        LOGGER.info("Finding orders by userId: {}", userId);
        List<Order> orders = orderService.findOrdersByUserId(userId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
    
    @PostMapping(value = "/add")
    public ResponseEntity<Object> placeOrder(@RequestParam("cartId") String cartId,
            HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || "".equals(token)) {
            return new ResponseEntity<>(new ApiResponse(false, "Token not present"), HttpStatus.UNAUTHORIZED);
        }
        User user;
        try {
            user = restTemplate.postForObject(AUTHENTICATION_URL, token, User.class);
        }
        catch (AuthenticationFailException ex) {
            return new ResponseEntity<>(new ApiResponse(false, ex.getMessage()), HttpStatus.UNAUTHORIZED);
        }
        catch (Exception ex) {
            LOGGER.error("", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if ((user.getAccountNumber() == null) || (user.getAddress() == null)) {
            return new ResponseEntity<>(new ApiResponse(false, "You need bank account number and line address to make order"), HttpStatus.NOT_ACCEPTABLE);
        }
        LOGGER.info("Fetching cart items (cartId = {}) using FeignClient", cartId);
        List<CartItem> CartItemsList = cartClientService.getOrderCartItems(cartId);
        if (CartItemsList.isEmpty()) {
            LOGGER.info("Cart (cartId = {}) is empty", cartId);
            return new ResponseEntity<>(new ApiResponse(false, "There are not found any items in your cart"), HttpStatus.BAD_REQUEST);
        }
        else {
            List<OrderItem> orderItems = new ArrayList<>();
            for (CartItem item : CartItemsList) {
                Product product = item.getProduct();
                OrderItem orderItem = new OrderItem(product.getId(), item.getQuantity(),
                        product.getPrice(), product.getName(), item.getSubTotalCost());
                orderItems.add(orderItem);
            }
            try {
                Order order = createOrder(orderItems, user);
                LOGGER.info("Saving new order: {}", order);
                orderService.saveOrder(order);
                OrderStream orderStream = createOrderStream(order, user.getAccountNumber());
                Message<OrderStream> checker = MessageBuilder.withPayload(orderStream).build();
                LOGGER.info("Sending stream order ({}) to RabbitMQ", orderStream);
                orderChecker.order().send(checker);
                return new ResponseEntity<>(order, HttpStatus.CREATED);
            }
            catch (Exception ex) {
                LOGGER.error("", ex);
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }
    
    @PostMapping("/create")
    public ResponseEntity<Object> makeOrder(@RequestBody List<Item> items, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || "".equals(token)) {
            return new ResponseEntity<>(new ApiResponse(false, "Token not present"), HttpStatus.UNAUTHORIZED);
        }
        User user;
        try {
            user = restTemplate.postForObject(AUTHENTICATION_URL, token, User.class);
        }
        catch (AuthenticationFailException ex) {
            return new ResponseEntity<>(new ApiResponse(false, ex.getMessage()), HttpStatus.UNAUTHORIZED);
        }
        catch (Exception ex) {
            LOGGER.error("", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if ((user.getAccountNumber() == null) || (user.getAddress() == null)) {
            return new ResponseEntity<>(new ApiResponse(false, "You need bank account number and line address to make order"), HttpStatus.NOT_ACCEPTABLE);
        }
        if (items.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse(false, "There are not found any items in your request"), HttpStatus.BAD_REQUEST);
        }
        else {
            List<OrderItem> orderItems = new ArrayList<>();
            for (Item item : items) {
                LOGGER.info("Fetching product (id = {}) using FeignClient", item.getProductId());
                Product product = productClientService.getOrderProductById(item.getProductId());
                if (product == null) {
                    LOGGER.error("Product (id = {}) is not found", item.getProductId());
                    return new ResponseEntity<>(new ApiResponse(false, "Product (id = " + item.getProductId() + ") is not found"), HttpStatus.NOT_FOUND);
                }
                OrderItem orderItem = createOrderItem(item, product);
                orderItems.add(orderItem);
            }
            try {
                Order order = createOrder(orderItems, user);
                LOGGER.info("Saving new order: {}", order);
                orderService.saveOrder(order);
                OrderStream orderStream = createOrderStream(order, user.getAccountNumber());
                Message<OrderStream> checker = MessageBuilder.withPayload(orderStream).build(); 
                LOGGER.info("Sending stream order ({}) to RabbitMQ", orderStream);
                orderChecker.order().send(checker);
                return new ResponseEntity<>(order, HttpStatus.CREATED);
            }
            catch (Exception ex) {
                LOGGER.error("", ex);
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }
    
    private OrderItem createOrderItem(Item item, Product product) {
        OrderItem orderItem = new OrderItem();
        orderItem.setProductId(item.getProductId());
        orderItem.setQuantity(item.getQuantity());
        orderItem.setProductPrice(product.getPrice());
        orderItem.setProductName(product.getName());
        orderItem.setSubTotal(OrderItemUtility.getSubTotalForOrderItem(product, item.getQuantity()));
        return orderItem;
    }
    
    private Order createOrder(List<OrderItem> orderItems, User user) {
        Order order = new Order();
        order.setTrackingId(UUID.randomUUID());
        order.setCustomerAddress(user.getAddress());
        order.setOrderDate(new Date());
        order.setTotal(OrderUtility.getTotalForOrder(orderItems));
        order.setItems(orderItems);
        order.setUserId(user.getId());
        order.setStatus(Order.Status.Pending); //PAYMENT_EXPECTED
        return order;
    }
    
    private OrderStream createOrderStream(Order order, String accountNumber) {
        OrderStream orderStream = new OrderStream();
        orderStream.setTrackingId(order.getTrackingId());
        orderStream.setCost(order.getTotal().doubleValue());
        orderStream.setAccountNumber(accountNumber);
        return orderStream;
    }
    
}
