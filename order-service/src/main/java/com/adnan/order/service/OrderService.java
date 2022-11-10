package com.adnan.order.service;

import com.adnan.order.entity.Order;
import com.adnan.order.repository.OrderRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    public List<Order> getAllOrders() {
        return orderRepository.findAll(Sort.by("orderDate").descending());
    }
    
    public List<Order> findOrdersByUserId(Long userId) {
        return orderRepository.findAllByUserIdOrderByOrderDateDesc(userId);
    }
    
    public Order findOrderByTrackingId(UUID trackingId) {
        return orderRepository.findByTrackingId(trackingId);
    }
    
    public Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId).get();
    }
    
    public void saveOrder(Order order) {
        orderRepository.save(order);
    }
    
}
