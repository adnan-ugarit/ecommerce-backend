package com.adnan.order.repository;

import com.adnan.order.entity.Order;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    public List<Order> findAllByUserIdOrderByOrderDateDesc(Long userId);
    public Order findByTrackingId(UUID trackingId);
    
}
