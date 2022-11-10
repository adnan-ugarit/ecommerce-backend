package com.adnan.catalog.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class CategoryDtoTest {
    @Test
    public void testSetId() {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(123L);
        assertEquals(123L, categoryDto.getId().longValue());
    }

    @Test
    public void testSetName() {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("Name");
        assertEquals("Name", categoryDto.getName());
    }

    @Test
    public void testSetDescription() {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setDescription("The characteristics of someone or something");
        assertEquals("The characteristics of someone or something", categoryDto.getDescription());
    }

    @Test
    public void testSetImageUrl() {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setImageUrl("https://example.org/example");
        assertEquals("https://example.org/example", categoryDto.getImageUrl());
    }
}

