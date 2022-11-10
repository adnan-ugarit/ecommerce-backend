package com.adnan.payment.client;

import com.adnan.payment.model.CheckoutItem;
import java.util.List;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("order-service")
public interface OrderClient {
    
    @GetMapping("/orders/{trackingId}")
    public List<CheckoutItem> fetchOrderItemsByTrackingId(@PathVariable("trackingId") UUID trackingId);
    
}
