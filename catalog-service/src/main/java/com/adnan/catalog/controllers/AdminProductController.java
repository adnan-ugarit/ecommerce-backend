package com.adnan.catalog.controllers;

import com.adnan.catalog.common.ApiResponse;
import com.adnan.catalog.dto.ProductDto;
import com.adnan.catalog.entities.Category;
import com.adnan.catalog.entities.Product;
import com.adnan.catalog.services.CategoryService;
import com.adnan.catalog.services.ProductService;
import com.adnan.catalog.utils.FileUtil;
import com.adnan.catalog.utils.JwtTokenUtil;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/admin/products")
public class AdminProductController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminProductController.class);
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    
    @GetMapping("")
    public ResponseEntity<List<ProductDto>> getAllProducts(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || "".equals(token)) {
            throw new RuntimeException("Token not present");
        }
        String role;
        try {
            role = jwtTokenUtil.getRole(token);
        }
        catch (Exception ex) {
            throw new RuntimeException("Token not valid");
        }
        if (!(("PRODUCT_MANAGER".equals(role)) || ("MARKETING_MANAGER".equals(role))  || ("ADMIN".equals(role)))) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        LOGGER.info("Finding all products");
        List<ProductDto> body = productService.getAllProducts();
        return new ResponseEntity<>(body, HttpStatus.OK);
    }
    
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody ProductDto productDto,
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
        Optional<Category> optionalCategory = categoryService.findCategoryById(productDto.getCategoryId());
        if (!optionalCategory.isPresent()) {
            LOGGER.error("Category (id = {}) is not found", productDto.getCategoryId());
            return new ResponseEntity<>(new ApiResponse(false, "Category is not found"), HttpStatus.NOT_FOUND);
        }
        Category category = optionalCategory.get();
        try {
            LOGGER.info("Adding product: {}", productDto);
            productService.addProduct(productDto, category);
            return new ResponseEntity<>(new ApiResponse(true, "Product has been added"), HttpStatus.CREATED);
        }
        catch (Exception ex) {
            LOGGER.error("", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PutMapping("/update/{productId}")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable("productId") Long productId,
            @RequestBody ProductDto productDto, HttpServletRequest request) {
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
        Optional<Product> optionalProduct = productService.findProductById(productId);
        if (!optionalProduct.isPresent()) {
            LOGGER.error("Product (id = {}) is not found", productId);
            return new ResponseEntity<>(new ApiResponse(false, "Product is not found"), HttpStatus.NOT_FOUND);
        }
        Optional<Category> optionalCategory = categoryService.findCategoryById(productDto.getCategoryId());
        if (!optionalCategory.isPresent()) {
            LOGGER.error("Category (id = {}) is not found", productDto.getCategoryId());
            return new ResponseEntity<>(new ApiResponse(false, "Category is not found"), HttpStatus.NOT_FOUND);
        }
        Category category = optionalCategory.get();
        try {
            LOGGER.info("Updating product: {}", productDto);
            productService.updateProduct(productId, productDto, category);
            return new ResponseEntity<>(new ApiResponse(true, "Product has been updated"), HttpStatus.OK);
        }
        catch (Exception ex) {
            LOGGER.error("", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable("productId") Long productId,
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
        Optional<Product> optionalProduct = productService.findProductById(productId);
        if (!optionalProduct.isPresent()) {
            LOGGER.error("Product (id = {}) is not found", productId);
            return new ResponseEntity<>(new ApiResponse(false, "Product is not found"), HttpStatus.NOT_FOUND);
        }
        Product product = optionalProduct.get();
        try {
            LOGGER.info("Deleting product: {}", product);
            productService.deleteProduct(productId);
            return new ResponseEntity<>(new ApiResponse(true, "Product has been deleted"), HttpStatus.OK);
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
        String uploadDir = "uploaded-images/products";
        try {
            LOGGER.info("Storing product image");
            String uploadedFilePath = FileUtil.storeFile(uploadDir, multipartFile);
            return new ResponseEntity<>(uploadedFilePath, HttpStatus.OK);
        }
        catch (IOException ex) {
            LOGGER.error("", ex);
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
}
