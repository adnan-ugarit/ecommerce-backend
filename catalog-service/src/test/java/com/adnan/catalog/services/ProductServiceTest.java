package com.adnan.catalog.services;

import com.adnan.catalog.dto.ProductDto;
import com.adnan.catalog.entities.Category;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.AdditionalMatchers.or;
import static org.mockito.Mockito.isA;
import static org.mockito.Mockito.isNull;
import static org.mockito.Mockito.when;

import com.adnan.catalog.entities.Product;
import com.adnan.catalog.repositories.ProductRepository;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {ProductService.class})
@ExtendWith(SpringExtension.class)
public class ProductServiceTest {
    @Autowired
    private ProductService productService;

    @MockBean
    private ProductRepository productRepository;

    @Test
    public void testGetAllProducts() {
        when(this.productRepository.findAll()).thenReturn(new ArrayList<Product>());
        assertEquals(0, this.productService.getAllProducts().size());
    }

    @Test
    public void testGetAvailableProducts() {
        when(this.productRepository.findAll()).thenReturn(new ArrayList<Product>());
        assertEquals(0, this.productService.getAvailableProducts().size());
    }

    @Test
    public void testFindProductById() {
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
        when(this.productRepository.findById(or(isA(Long.class), isNull()))).thenReturn(Optional.<Product>of(product));
        assertSame(product, this.productService.findProductById(123L).get());
    }

    @Test
    public void testAddProduct() {
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
        this.productService.addProduct(productDto, category);
    }

    @Test
    public void testUpdateProduct() {
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
        this.productService.updateProduct(123L, productDto, category);
    }

    @Test
    public void testDeleteProduct() {
        this.productService.deleteProduct(123L);
    }
}

