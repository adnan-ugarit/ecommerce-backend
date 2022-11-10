package com.adnan.catalog.controllers;

import com.adnan.catalog.dto.CategoryDto;
import com.adnan.catalog.entities.Category;
import com.adnan.catalog.mappers.CategoryMapper;
import com.adnan.catalog.services.CategoryService;
import com.adnan.catalog.utils.FileUtil;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories")
@CrossOrigin(origins = "http://localhost:3000")
public class CategoryController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);
    
    @Autowired
    private CategoryService categoryService;
    
    @GetMapping("")
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        LOGGER.info("Finding all categories");
        List<CategoryDto> body = categoryService.getAllCategories();
        return new ResponseEntity<>(body, HttpStatus.OK);
    }
    
    /*@GetMapping("/{categoryName}")
    public ResponseEntity<CategoryDto> findCategoryByName(@PathVariable("categoryName") String categoryName) {
        LOGGER.info("Finding Category by name: {}", categoryName);
        Optional<Category> optionalCategory = categoryService.findCategoryByName(categoryName);
        if (!optionalCategory.isPresent()) {
            LOGGER.info("Category (name = {}) is not found", categoryName);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Category category = optionalCategory.get();
        CategoryDto body = CategoryMapper.getDtoFromCategory(category);
        return new ResponseEntity<>(body, HttpStatus.OK);
    }*/
    
    @GetMapping("/uploaded-images/categories/{fileName:.+}")
    public ResponseEntity<Resource> downloadImage(@PathVariable String fileName) {
        String downloadDir = "uploaded-images/categories";
        LOGGER.info("Loading category image");
        Resource file = FileUtil.loadFile(downloadDir, fileName);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }
    
}
