package com.adnan.order.stream;

import com.adnan.order.entity.Order;
import com.adnan.order.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

@Component
public class OrderListener {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderListener.class);
    
    @Autowired
    private OrderService orderService;
    
    @StreamListener(target = "resultChannel")
    public void orderResult(OrderStream orderStream) {
        LOGGER.info("Receiving stream order ({}) from RabbitMQ", orderStream);
        LOGGER.info("Finding order by trackingId: {}", orderStream.getTrackingId());
        Order order = orderService.findOrderByTrackingId(orderStream.getTrackingId());
        if (orderStream.getStatus() == OrderStream.Status.Approved)
            order.setStatus(Order.Status.Approved);
        else
            order.setStatus(Order.Status.Declined);
        try {
            LOGGER.info("Updating order ({}) status to ({})", order, orderStream.getStatus());
            orderService.saveOrder(order);
        }
        catch (Exception ex) {
            LOGGER.error("", ex);
        }
    }
    
}
