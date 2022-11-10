package com.adnan.catalog.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ProductDtoTest {
    @Test
    public void testConstructor() {
        ProductDto actualProductDto = new ProductDto("Name", "The characteristics of someone or something",
                "https://example.org/example", 10.0, true, 123L);
        assertEquals("Name", actualProductDto.getName());
        assertEquals(10.0, actualProductDto.getPrice());
        assertEquals("The characteristics of someone or something", actualProductDto.getDescription());
        assertTrue(actualProductDto.isInStock());
        assertEquals(123L, actualProductDto.getCategoryId().longValue());
        assertEquals("https://example.org/example", actualProductDto.getImageUrl());
    }
}

