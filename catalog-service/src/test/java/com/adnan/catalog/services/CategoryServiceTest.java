package com.adnan.catalog.services;

import com.adnan.catalog.dto.CategoryDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.AdditionalMatchers.or;
import static org.mockito.Mockito.isA;
import static org.mockito.Mockito.isNull;
import static org.mockito.Mockito.when;

import com.adnan.catalog.entities.Category;
import com.adnan.catalog.repositories.CategoryRepository;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {CategoryService.class})
@ExtendWith(SpringExtension.class)
public class CategoryServiceTest {
    @Autowired
    private CategoryService categoryService;

    @MockBean
    private CategoryRepository categoryRepository;

    @Test
    public void testGetAllCategories() {
        when(this.categoryRepository.findAll()).thenReturn(new ArrayList<Category>());
        assertEquals(0, this.categoryService.getAllCategories().size());
    }

    @Test
    public void testFindCategoryById() {
        Category category = new Category();
        category.setImageUrl("https://example.org/example");
        category.setId(123L);
        category.setName("Name");
        category.setDescription("The characteristics of someone or something");
        when(this.categoryRepository.findById(or(isA(Long.class), isNull()))).thenReturn(Optional.<Category>of(category));
        assertSame(category, this.categoryService.findCategoryById(123L).get());
    }

    @Test
    public void testFindCategoryByName() {
        Category category = new Category();
        category.setImageUrl("https://example.org/example");
        category.setId(123L);
        category.setName("Name");
        category.setDescription("The characteristics of someone or something");
        when(this.categoryRepository.findCategoryByName(or(isA(String.class), isNull()))).thenReturn(Optional.<Category>of(category));
        assertSame(category, this.categoryService.findCategoryByName("Name").get());
    }

    @Test
    public void testAddCategory() {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setImageUrl("https://example.org/example");
        categoryDto.setId(123L);
        categoryDto.setName("Name");
        categoryDto.setDescription("The characteristics of someone or something");
        this.categoryService.addCategory(categoryDto);
    }

    @Test
    public void testUpdateCategory() {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setImageUrl("https://example.org/example");
        categoryDto.setId(123L);
        categoryDto.setName("Name");
        categoryDto.setDescription("The characteristics of someone or something");
        this.categoryService.updateCategory(123L, categoryDto);
    }

    @Test
    public void testDeleteCategory() {
        this.categoryService.deleteCategory(123L);
    }
}

