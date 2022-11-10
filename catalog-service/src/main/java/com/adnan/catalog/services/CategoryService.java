package com.adnan.catalog.services;

import com.adnan.catalog.dto.CategoryDto;
import com.adnan.catalog.entities.Category;
import com.adnan.catalog.mappers.CategoryMapper;
import com.adnan.catalog.repositories.CategoryRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CategoryService {
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryDto> categoryDtos = new ArrayList<>();
        for (Category category : categories) {
            CategoryDto categoryDto = CategoryMapper.getDtoFromCategory(category);
            categoryDtos.add(categoryDto);
        }
        return categoryDtos;
    }
    
    public Optional<Category> findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId);
    }
    
    public Optional<Category> findCategoryByName(String categoryName) {
        return categoryRepository.findCategoryByName(categoryName);
    }
    
    public void addCategory(CategoryDto categoryDto) {
        Category category = CategoryMapper.getCategoryFromDto(categoryDto);
        categoryRepository.save(category);
    }
    
    public void updateCategory(Long categoryId, CategoryDto categoryDto) {
        Category category = CategoryMapper.getCategoryFromDto(categoryDto);
        category.setId(categoryId);
        categoryRepository.save(category);
    }
    
    public void deleteCategory(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }
    
}
