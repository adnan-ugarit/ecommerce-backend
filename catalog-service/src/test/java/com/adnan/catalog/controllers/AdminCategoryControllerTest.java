package com.adnan.catalog.controllers;

import com.adnan.catalog.dto.CategoryDto;
import static org.mockito.AdditionalMatchers.or;
import static org.mockito.Mockito.isA;
import static org.mockito.Mockito.isNull;
import static org.mockito.Mockito.when;

import com.adnan.catalog.dto.ProductDto;
import com.adnan.catalog.entities.Category;
import com.adnan.catalog.mappers.CategoryMapper;
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

//@ContextConfiguration(classes = {AdminCategoryController.class})
//@ExtendWith(SpringExtension.class)
public class AdminCategoryControllerTest {
    @Autowired
    private AdminCategoryController adminCategoryController;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private ProductService productService;

    //@Test
    public void testAddCategory() throws Exception {
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders
                .post("/admin/categories/add").contentType(MediaType.APPLICATION_JSON);
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setImageUrl("https://example.org/example");
        categoryDto.setId(123L);
        categoryDto.setName("Name");
        categoryDto.setDescription("The characteristics of someone or something");
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult
                .content((new ObjectMapper()).writeValueAsString(categoryDto));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.adminCategoryController).build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated());
    }

    //@Test
    public void testUpdateProduct() throws Exception {
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders
                .put("/admin/categories/update/{categoryId}", 1L).contentType(MediaType.APPLICATION_JSON);
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setImageUrl("https://example.org/example");
        categoryDto.setId(123L);
        categoryDto.setName("Name");
        categoryDto.setDescription("The characteristics of someone or something");
        Category category = CategoryMapper.getCategoryFromDto(categoryDto);
        when(this.categoryService.findCategoryById(or(isA(Long.class), isNull()))).thenReturn(Optional.<Category>of(category));
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult
                .content((new ObjectMapper()).writeValueAsString(categoryDto));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.adminCategoryController).build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isOk());
    }

    //@Test
    public void testDeleteCategory() throws Exception {
        Category category = new Category();
        category.setImageUrl("https://example.org/example");
        category.setId(123L);
        category.setName("Name");
        category.setDescription("The characteristics of someone or something");
        when(this.categoryService.findCategoryById(or(isA(Long.class), isNull())))
                .thenReturn(Optional.<Category>of(category));        
        when(this.productService.getAllProducts()).thenReturn(new ArrayList<ProductDto>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/admin/categories/delete/{categoryId}", 1L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.adminCategoryController).build()
                .perform(requestBuilder);
        ResultActions resultActions = actualPerformResult.andExpect(MockMvcResultMatchers.status().isOk());
        ResultActions resultActions1 = resultActions
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
        Matcher<String> matcher = Matchers.containsString("{\"success\":true,\"message\":\"Category has been deleted\"}");
        resultActions1.andExpect(MockMvcResultMatchers.content().string(matcher));
    }

    //@Test
    public void testDeleteCategory2() throws Exception {
        when(this.categoryService.findCategoryById(or(isA(Long.class), isNull()))).thenReturn(Optional.<Category>empty());
        when(this.productService.getAllProducts()).thenReturn(new ArrayList<ProductDto>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/admin/categories/delete/{categoryId}", 1L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.adminCategoryController).build()
                .perform(requestBuilder);
        ResultActions resultActions = actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
        ResultActions resultActions1 = resultActions
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
        Matcher<String> matcher = Matchers.containsString("{\"success\":false,\"message\":\"Category is not found\"}");
        resultActions1.andExpect(MockMvcResultMatchers.content().string(matcher));
    }
    
    @Test
    void contextLoads() {
    }
}

