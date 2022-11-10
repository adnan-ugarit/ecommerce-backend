package com.adnan.catalog.controllers;

import static org.mockito.AdditionalMatchers.or;
import static org.mockito.Mockito.isA;
import static org.mockito.Mockito.isNull;
import static org.mockito.Mockito.when;

import com.adnan.catalog.dto.ProductDto;
import com.adnan.catalog.entities.Category;
import com.adnan.catalog.entities.Product;
import com.adnan.catalog.mappers.ProductMapper;
import com.adnan.catalog.services.CategoryService;
import com.adnan.catalog.services.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

import java.util.Optional;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

//@ContextConfiguration(classes = {AdminProductController.class})
//@ExtendWith(SpringExtension.class)
public class AdminProductControllerTest {
    @Autowired
    private AdminProductController adminProductController;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private ProductService productService;

    //@Test
    public void testGetAllProducts() throws Exception {
        ProductDto productDto = new ProductDto();
        productDto.setCategoryId(1L);
        productDto.setPrice(10.0);
        productDto.setInStock(true);
        productDto.setImageUrl("https://example.org/example");
        productDto.setId(123L);
        productDto.setName("Name");
        productDto.setDescription("The characteristics of someone or something");
        ArrayList<ProductDto> productDtos = new ArrayList<>();
        productDtos.add(productDto);
        when(this.productService.getAllProducts()).thenReturn(productDtos);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/admin/products");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.adminProductController).build()
                .perform(requestBuilder);
        actualPerformResult
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Name"));
    }

    //@Test
    public void testGetAllProducts2() throws Exception {
        when(this.productService.getAllProducts()).thenReturn(new ArrayList<ProductDto>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/admin/products");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.adminProductController).build()
                .perform(requestBuilder);
        ResultActions resultActions = actualPerformResult.andExpect(MockMvcResultMatchers.status().isOk());
        ResultActions resultActions1 = resultActions
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
        Matcher<String> matcher = Matchers.containsString("[]");
        resultActions1.andExpect(MockMvcResultMatchers.content().string(matcher));
    }

    //@Test
    public void testAddProduct() throws Exception {
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders
                .post("/admin/products/add").contentType(MediaType.APPLICATION_JSON);
        Category category = new Category();
        category.setImageUrl("https://example.org/example");
        category.setId(123L);
        category.setName("Name");
        category.setDescription("The characteristics of someone or something");
        when(this.categoryService.findCategoryById(or(isA(Long.class), isNull()))).thenReturn(Optional.<Category>of(category));
        ProductDto productDto = new ProductDto();
        productDto.setCategoryId(category.getId());
        productDto.setPrice(10.0);
        productDto.setInStock(true);
        productDto.setImageUrl("https://example.org/example");
        productDto.setId(123L);
        productDto.setName("Name");
        productDto.setDescription("The characteristics of someone or something");
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult
                .content((new ObjectMapper()).writeValueAsString(productDto));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.adminProductController).build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated());
    }

    //@Test
    public void testUpdateProduct() throws Exception {
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders
                .put("/admin/products/update/{productId}", 1L).contentType(MediaType.APPLICATION_JSON);
        Category category = new Category();
        category.setImageUrl("https://example.org/example");
        category.setId(123L);
        category.setName("Name");
        category.setDescription("The characteristics of someone or something");
        when(this.categoryService.findCategoryById(or(isA(Long.class), isNull()))).thenReturn(Optional.<Category>of(category));
        ProductDto productDto = new ProductDto();
        productDto.setCategoryId(category.getId());
        productDto.setPrice(10.0);
        productDto.setInStock(true);
        productDto.setImageUrl("https://example.org/example");
        productDto.setId(123L);
        productDto.setName("Name");
        productDto.setDescription("The characteristics of someone or something");
        Product product = ProductMapper.getProductFromDto(productDto, category);
        when(this.productService.findProductById(or(isA(Long.class), isNull()))).thenReturn(Optional.<Product>of(product));
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult
                .content((new ObjectMapper()).writeValueAsString(productDto));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.adminProductController).build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isOk());
    }

    //@Test
    public void testDeleteProduct() throws Exception {
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
        when(this.productService.findProductById(or(isA(Long.class), isNull()))).thenReturn(Optional.<Product>of(product));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/admin/products/delete/{productId}",
                1L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.adminProductController).build()
                .perform(requestBuilder);
        ResultActions resultActions = actualPerformResult.andExpect(MockMvcResultMatchers.status().isOk());
        ResultActions resultActions1 = resultActions
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
        Matcher<String> matcher = Matchers.containsString("{\"success\":true,\"message\":\"Product has been deleted\"}");
        resultActions1.andExpect(MockMvcResultMatchers.content().string(matcher));
    }

    //@Test
    public void testDeleteProduct2() throws Exception {
        when(this.productService.findProductById(or(isA(Long.class), isNull()))).thenReturn(Optional.<Product>empty());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/admin/products/delete/{productId}",
                1L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.adminProductController).build()
                .perform(requestBuilder);
        ResultActions resultActions = actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
        ResultActions resultActions1 = resultActions
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
        Matcher<String> matcher = Matchers.containsString("{\"success\":false,\"message\":\"Product is not found\"}");
        resultActions1.andExpect(MockMvcResultMatchers.content().string(matcher));
    }
    
    @Test
    void contextLoads() {
    }
}

