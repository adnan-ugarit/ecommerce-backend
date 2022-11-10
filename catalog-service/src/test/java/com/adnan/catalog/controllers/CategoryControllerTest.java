package com.adnan.catalog.controllers;

import static org.mockito.Mockito.when;

import com.adnan.catalog.dto.CategoryDto;
import com.adnan.catalog.services.CategoryService;

import java.util.ArrayList;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {CategoryController.class})
@ExtendWith(SpringExtension.class)
public class CategoryControllerTest {
    @Autowired
    private CategoryController categoryController;

    @MockBean
    private CategoryService categoryService;

    @Test
    public void testGetAllProducts() throws Exception {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setImageUrl("https://example.org/example");
        categoryDto.setId(123L);
        categoryDto.setName("Name");
        categoryDto.setDescription("The characteristics of someone or something");
        ArrayList<CategoryDto> categoryDtos = new ArrayList<>();
        categoryDtos.add(categoryDto);
        when(this.categoryService.getAllCategories()).thenReturn(categoryDtos);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/categories");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.categoryController).build()
                .perform(requestBuilder);
        actualPerformResult
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Name"));
    }

    @Test
    public void testGetAllProducts2() throws Exception {
        when(this.categoryService.getAllCategories()).thenReturn(new ArrayList<CategoryDto>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/categories");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.categoryController).build()
                .perform(requestBuilder);
        ResultActions resultActions = actualPerformResult.andExpect(MockMvcResultMatchers.status().isOk());
        ResultActions resultActions1 = resultActions
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
        Matcher<String> matcher = Matchers.containsString("[]");
        resultActions1.andExpect(MockMvcResultMatchers.content().string(matcher));
    }

    @Test
    public void testDownloadImage() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/categories/uploaded-images/categories/{fileName:.+}",
                "photo-1592067284261-cb1092d519ba.jpg");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.categoryController).build()
                .perform(requestBuilder);
        ResultActions resultActions = actualPerformResult.andExpect(MockMvcResultMatchers.status().isOk());
        resultActions.andExpect(MockMvcResultMatchers.content().contentType("application/octet-stream"));
    }
}

