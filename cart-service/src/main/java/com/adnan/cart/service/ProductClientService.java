package com.adnan.cart.service;

import com.adnan.cart.client.ProductClient;
import com.adnan.cart.model.Product;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductClientService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductClientService.class);
    
    @Autowired
    private ProductClient productClient;
    
    @HystrixCommand(fallbackMethod = "getDefaultCartProductById")
    public Product getCartProductById(Long productId) {
        return productClient.fetchProductById(productId);
    }
    
    Product getDefaultCartProductById(Long productId) {
        LOGGER.info("Returning default product for productId: {}", productId);
        Product product = new Product();
        product.setId(productId);
        product.setName("Default Product");
        product.setImageUrl("https://example.org/example");
        product.setPrice(0);
        return product;
    }
    
}
