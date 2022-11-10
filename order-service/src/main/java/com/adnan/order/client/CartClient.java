package com.adnan.order.client;

import com.adnan.order.model.CartItem;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient("cart-service")
public interface CartClient {
    
    @GetMapping("/cart/fetch")
    public List<CartItem> fetchCartItems(@RequestHeader("cookie") String cartId);
    
}
