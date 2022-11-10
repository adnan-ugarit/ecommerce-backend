package com.adnan.catalog.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.adnan.catalog.dto.CategoryDto;
import com.adnan.catalog.entities.Category;
import org.junit.jupiter.api.Test;

public class CategoryMapperTest {
    @Test
    public void testGetDtoFromCategory() {
        Category category = new Category();
        category.setImageUrl("https://example.org/example");
        category.setId(123L);
        category.setName("Name");
        category.setDescription("The characteristics of someone or something");
        CategoryDto actualDtoFromCategory = CategoryMapper.getDtoFromCategory(category);
        assertEquals("Name", actualDtoFromCategory.getName());
        assertEquals(123L, actualDtoFromCategory.getId().longValue());
        assertEquals("The characteristics of someone or something", actualDtoFromCategory.getDescription());
        assertEquals("https://example.org/example", actualDtoFromCategory.getImageUrl());
    }

    @Test
    public void testGetCategoryFromDto() {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setImageUrl("https://example.org/example");
        categoryDto.setId(123L);
        categoryDto.setName("Name");
        categoryDto.setDescription("The characteristics of someone or something");
        Category actualCategoryFromDto = CategoryMapper.getCategoryFromDto(categoryDto);
        assertEquals("Name", actualCategoryFromDto.getName());
        assertEquals("The characteristics of someone or something", actualCategoryFromDto.getDescription());
        assertEquals("https://example.org/example", actualCategoryFromDto.getImageUrl());
    }
}

