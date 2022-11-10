package com.adnan.catalog.controllers;

import com.adnan.catalog.common.ApiResponse;
import com.adnan.catalog.dto.CategoryDto;
import com.adnan.catalog.dto.ProductDto;
import com.adnan.catalog.entities.Category;
import com.adnan.catalog.services.CategoryService;
import com.adnan.catalog.services.ProductService;
import com.adnan.catalog.utils.FileUtil;
import com.adnan.catalog.utils.JwtTokenUtil;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/admin/categories")
public class AdminCategoryController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminCategoryController.class);
    
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addCategory(@RequestBody CategoryDto categoryDto,
            HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || "".equals(token)) {
            return new ResponseEntity<>(new ApiResponse(false, "Token not present"), HttpStatus.UNAUTHORIZED);
        }
        String role;
        try {
            role = jwtTokenUtil.getRole(token);
        }
        catch (Exception ex) {
            return new ResponseEntity<>(new ApiResponse(false, "Token not valid"), HttpStatus.UNAUTHORIZED);
        }
        if (!(("PRODUCT_MANAGER".equals(role)) || ("ADMIN".equals(role)))) {
            return new ResponseEntity<>(new ApiResponse(false, "You don't have proper permits"), HttpStatus.FORBIDDEN);
        }
        Optional<Category> optionalCategory = categoryService.findCategoryByName(categoryDto.getName());
        if (optionalCategory.isPresent()) {
            LOGGER.error("Category with same name ({}) is found", categoryDto.getName());
            return new ResponseEntity<>(new ApiResponse(false, "Category already exists"), HttpStatus.CONFLICT);
        }
        try {
            LOGGER.info("Adding category: {}", categoryDto);
            categoryService.addCategory(categoryDto);
            return new ResponseEntity<>(new ApiResponse(true, "Category has been added"), HttpStatus.CREATED);
        }
        catch (Exception ex) {
            LOGGER.error("", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PutMapping("/update/{categoryId}")
    public ResponseEntity<ApiResponse> updateCategory(@PathVariable("categoryId") Long categoryId,
            @RequestBody CategoryDto categoryDto, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || "".equals(token)) {
            return new ResponseEntity<>(new ApiResponse(false, "Token not present"), HttpStatus.UNAUTHORIZED);
        }
        String role;
        try {
            role = jwtTokenUtil.getRole(token);
        }
        catch (Exception ex) {
            return new ResponseEntity<>(new ApiResponse(false, "Token not valid"), HttpStatus.UNAUTHORIZED);
        }
        if (!(("PRODUCT_MANAGER".equals(role)) || ("ADMIN".equals(role)))) {
            return new ResponseEntity<>(new ApiResponse(false, "You don't have proper permits"), HttpStatus.FORBIDDEN);
        }
        Optional<Category> optionalCategory = categoryService.findCategoryById(categoryId);
        if (!optionalCategory.isPresent()) {
            LOGGER.error("Category (id = {}) is not found", categoryId);
            return new ResponseEntity<>(new ApiResponse(false, "Category is not found"), HttpStatus.NOT_FOUND);
        }
        try {
            LOGGER.info("Updating category: {}", categoryDto);
            categoryService.updateCategory(categoryId, categoryDto);
            return new ResponseEntity<>(new ApiResponse(true, "Category has been updated"), HttpStatus.OK);
        }
        catch (Exception ex) {
            LOGGER.error("", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @DeleteMapping("/delete/{categoryId}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable("categoryId") Long categoryId,
            HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || "".equals(token)) {
            return new ResponseEntity<>(new ApiResponse(false, "Token not present"), HttpStatus.UNAUTHORIZED);
        }
        String role;
        try {
            role = jwtTokenUtil.getRole(token);
        }
        catch (Exception ex) {
            return new ResponseEntity<>(new ApiResponse(false, "Token not valid"), HttpStatus.UNAUTHORIZED);
        }
        if (!(("PRODUCT_MANAGER".equals(role)) || ("ADMIN".equals(role)))) {
            return new ResponseEntity<>(new ApiResponse(false, "You don't have proper permits"), HttpStatus.FORBIDDEN);
        }
        Optional<Category> optionalCategory = categoryService.findCategoryById(categoryId);
        if (!optionalCategory.isPresent()) {
            LOGGER.error("Category (id = {}) is not found", categoryId);
            return new ResponseEntity<>(new ApiResponse(false, "Category is not found"), HttpStatus.NOT_FOUND);
        }
        Category category = optionalCategory.get();
        List<ProductDto> products = productService.getAllProducts();
        for (ProductDto product : products) {
            if (Objects.equals(product.getCategoryId(), categoryId)) {
                LOGGER.error("Product ({}) associated with this category is found", product);
                return new ResponseEntity<>(new ApiResponse(false, "There are products associated with this category"), HttpStatus.CONFLICT);
            }
        }
        try {
            LOGGER.info("Deleting category: {}", category);
            categoryService.deleteCategory(categoryId);
            return new ResponseEntity<>(new ApiResponse(true, "Category has been deleted"), HttpStatus.OK);
        }
        catch (Exception ex) {
            LOGGER.error("", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping("/imageUpload")
    public ResponseEntity<String> handleFileUpload(@RequestPart("image") MultipartFile multipartFile,
            HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || "".equals(token)) {
            return new ResponseEntity<>("Token not present", HttpStatus.UNAUTHORIZED);
        }
        String role;
        try {
            role = jwtTokenUtil.getRole(token);
        }
        catch (Exception ex) {
            return new ResponseEntity<>("Token not valid", HttpStatus.UNAUTHORIZED);
        }
        if (!(("PRODUCT_MANAGER".equals(role)) || ("ADMIN".equals(role)))) {
            return new ResponseEntity<>("You don't have proper permits", HttpStatus.FORBIDDEN);
        }
        String uploadDir = "uploaded-images/categories";
        try {
            LOGGER.info("Storing category image");
            String uploadedFilePath = FileUtil.storeFile(uploadDir, multipartFile);
            return new ResponseEntity<>(uploadedFilePath, HttpStatus.OK);
        }
        catch (IOException ex) {
            LOGGER.error("", ex);
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
}
