package com.adnan.catalog.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ProductTest {
    @Test
    public void testSetId() {
        Product product = new Product();
        product.setId(123L);
        assertEquals(123L, product.getId().longValue());
    }

    @Test
    public void testSetName() {
        Product product = new Product();
        product.setName("Name");
        assertEquals("Name", product.getName());
    }

    @Test
    public void testSetDescription() {
        Product product = new Product();
        product.setDescription("The characteristics of someone or something");
        assertEquals("The characteristics of someone or something", product.getDescription());
    }

    @Test
    public void testSetImageUrl() {
        Product product = new Product();
        product.setImageUrl("https://example.org/example");
        assertEquals("https://example.org/example", product.getImageUrl());
    }

    @Test
    public void testSetPrice() {
        Product product = new Product();
        product.setPrice(10.0);
        assertEquals(10.0, product.getPrice());
    }

    @Test
    public void testSetInStock() {
        Product product = new Product();
        product.setInStock(true);
        assertTrue(product.isInStock());
    }
    
    @Test
    public void testSetCategory() {
        Category category = new Category();
        category.setImageUrl("https://example.org/example");
        category.setId(123L);
        category.setName("Name");
        category.setDescription("The characteristics of someone or something");
        Product product = new Product();
        product.setCategory(category);
        assertSame(category, product.getCategory());
    }
}

