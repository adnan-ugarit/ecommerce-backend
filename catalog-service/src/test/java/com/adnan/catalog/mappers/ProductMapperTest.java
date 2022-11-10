package com.adnan.catalog.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.adnan.catalog.dto.ProductDto;
import com.adnan.catalog.entities.Category;
import com.adnan.catalog.entities.Product;
import org.junit.jupiter.api.Test;

public class ProductMapperTest {
    @Test
    public void testGetDtoFromProduct() {
        Category category = new Category();
        category.setImageUrl("https://example.org/example");
        category.setId(123L);
        category.setName("Name");
        category.setDescription("The characteristics of someone or something");
        Product product = new Product();
        product.setCategory(category);
        product.setPrice(10.0);
        product.setInStock(true);
        product.setImageUrl("https://example.org/example");
        product.setId(123L);
        product.setName("Name");
        product.setDescription("The characteristics of someone or something");
        ProductDto actualDtoFromProduct = ProductMapper.getDtoFromProduct(product);
        assertEquals("Name", actualDtoFromProduct.getName());
        assertEquals(10.0, actualDtoFromProduct.getPrice());
        assertEquals(123L, actualDtoFromProduct.getId().longValue());
        assertEquals("The characteristics of someone or something", actualDtoFromProduct.getDescription());
        assertTrue(actualDtoFromProduct.isInStock());
        assertEquals(123L, actualDtoFromProduct.getCategoryId().longValue());
        assertEquals("https://example.org/example", actualDtoFromProduct.getImageUrl());
    }

    @Test
    public void testGetProductFromDto() {
        Category category = new Category();
        category.setImageUrl("https://example.org/example");
        category.setId(123L);
        category.setName("Name");
        category.setDescription("The characteristics of someone or something");
        ProductDto productDto = new ProductDto();
        productDto.setCategoryId(category.getId());
        productDto.setPrice(10.0);
        productDto.setInStock(true);
        productDto.setImageUrl("https://example.org/example");
        productDto.setId(123L);
        productDto.setName("Name");
        productDto.setDescription("The characteristics of someone or something");
        Product actualProductFromDto = ProductMapper.getProductFromDto(productDto, category);
        assertEquals("Name", actualProductFromDto.getName());
        assertEquals(10.0, actualProductFromDto.getPrice());
        assertEquals("The characteristics of someone or something", actualProductFromDto.getDescription());
        assertTrue(actualProductFromDto.isInStock());
        assertEquals("https://example.org/example", actualProductFromDto.getImageUrl());
        assertSame(category, actualProductFromDto.getCategory());
    }
}

