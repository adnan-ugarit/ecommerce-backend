package com.adnan.cart.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.AdditionalMatchers.or;
import static org.mockito.Mockito.isA;
import static org.mockito.Mockito.isNull;
import static org.mockito.Mockito.when;

import com.adnan.cart.client.ProductClient;
import com.adnan.cart.model.Product;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {ProductClientService.class})
@ExtendWith(SpringExtension.class)
public class ProductClientServiceTest {
    @MockBean
    private ProductClient productClient;

    @Autowired
    private ProductClientService productClientService;

    @Test
    public void testGetCartProductById() {
        Product product = new Product();
        product.setId(123L);
        product.setName("Name");
        product.setImageUrl("https://example.org/example");
        product.setPrice(10.0);
        when(this.productClient.fetchProductById(or(isA(Long.class), isNull()))).thenReturn(product);
        Product actualCartProductById = this.productClientService.getCartProductById(123L);
        assertSame(product, actualCartProductById);
    }

    @Test
    public void testGetDefaultCartProductById() {
        Product actualDefaultCartProductById = this.productClientService.getDefaultCartProductById(123L);
        assertEquals("Default Product", actualDefaultCartProductById.getName());
        assertEquals(0.0, actualDefaultCartProductById.getPrice());
        assertEquals(123L, actualDefaultCartProductById.getId().longValue());
        assertEquals("https://example.org/example", actualDefaultCartProductById.getImageUrl());
    }
}

