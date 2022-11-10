package com.adnan.catalog.mappers;

import com.adnan.catalog.dto.CategoryDto;
import com.adnan.catalog.entities.Category;

public class CategoryMapper {
    
    public static CategoryDto getDtoFromCategory(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        categoryDto.setDescription(category.getDescription());
        categoryDto.setImageUrl(category.getImageUrl());
        return categoryDto;
    }
    
    public static Category getCategoryFromDto(CategoryDto categoryDto) {
        Category category = new Category();
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());
        category.setImageUrl(categoryDto.getImageUrl());
        return category;
    }
    
}
