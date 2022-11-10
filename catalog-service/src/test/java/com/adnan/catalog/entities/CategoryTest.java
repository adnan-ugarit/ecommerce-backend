package com.adnan.catalog.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class CategoryTest {
    @Test
    public void testSetId() {
        Category category = new Category();
        category.setId(123L);
        assertEquals(123L, category.getId().longValue());
    }

    @Test
    public void testSetName() {
        Category category = new Category();
        category.setName("Name");
        assertEquals("Name", category.getName());
    }

    @Test
    public void testSetDescription() {
        Category category = new Category();
        category.setDescription("The characteristics of someone or something");
        assertEquals("The characteristics of someone or something", category.getDescription());
    }

    @Test
    public void testSetImageUrl() {
        Category category = new Category();
        category.setImageUrl("https://example.org/example");
        assertEquals("https://example.org/example", category.getImageUrl());
    }
}

