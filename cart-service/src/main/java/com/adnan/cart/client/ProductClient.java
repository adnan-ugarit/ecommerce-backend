package com.adnan.cart.client;

import com.adnan.cart.model.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("catalog-service")
public interface ProductClient {

    @GetMapping("/products/{productId}")
    public Product fetchProductById(@PathVariable("productId") Long productId);
    
}
