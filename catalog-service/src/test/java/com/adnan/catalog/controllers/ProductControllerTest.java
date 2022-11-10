package com.adnan.catalog.controllers;

import static org.mockito.AdditionalMatchers.or;
import static org.mockito.Mockito.isA;
import static org.mockito.Mockito.isNull;
import static org.mockito.Mockito.when;

import com.adnan.catalog.dto.ExternalProductDto;
import com.adnan.catalog.dto.ProductDto;
import com.adnan.catalog.entities.Category;
import com.adnan.catalog.entities.Product;
import com.adnan.catalog.metrics.TPMCounter;
import com.adnan.catalog.services.CategoryService;
import com.adnan.catalog.services.ExternalApiService;
import com.adnan.catalog.services.ProductService;
import io.micrometer.prometheus.PrometheusMeterRegistry;

import java.util.ArrayList;
import java.util.Optional;

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

@ContextConfiguration(classes = {ProductController.class})
@ExtendWith(SpringExtension.class)
public class ProductControllerTest {
    @Autowired
    private ProductController productController;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private ExternalApiService externalApiService;

    @MockBean
    private ProductService productService;

    @MockBean
    private PrometheusMeterRegistry prometheusMeterRegistry;

    @MockBean
    private TPMCounter tpm;

    @Test
    public void testGetAvailableProducts() throws Exception {
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
        when(this.productService.getAvailableProducts()).thenReturn(productDtos);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/products");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.productController).build()
                .perform(requestBuilder);
        actualPerformResult
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Name"));
    }

    @Test
    public void testGetAvailableProducts2() throws Exception {
        when(this.productService.getAvailableProducts()).thenReturn(new ArrayList<ProductDto>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/products");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.productController).build()
                .perform(requestBuilder);
        ResultActions resultActions = actualPerformResult.andExpect(MockMvcResultMatchers.status().isOk());
        ResultActions resultActions1 = resultActions
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
        Matcher<String> matcher = Matchers.containsString("[]");
        resultActions1.andExpect(MockMvcResultMatchers.content().string(matcher));
    }

    @Test
    public void testGetExternalProducts() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/products/external");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.productController).build()
                .perform(requestBuilder);
        ResultActions resultActions = actualPerformResult.andExpect(MockMvcResultMatchers.status().isOk());
        resultActions
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }

    @Test
    public void testGetExternalProducts2() throws Exception {
        when(this.externalApiService.consumeProducts()).thenReturn(new ArrayList<ExternalProductDto>());
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/products/external");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.productController).build()
                .perform(getResult);
        ResultActions resultActions = actualPerformResult.andExpect(MockMvcResultMatchers.status().isOk());
        ResultActions resultActions1 = resultActions
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
        Matcher<String> matcher = Matchers.containsString("[]");
        resultActions1.andExpect(MockMvcResultMatchers.content().string(matcher));
    }

    @Test
    public void testFindProductById() throws Exception {
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
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/products/{productId}", 1L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.productController).build()
                .perform(requestBuilder);
        ResultActions resultActions = actualPerformResult.andExpect(MockMvcResultMatchers.status().isOk());
        ResultActions resultActions1 = resultActions
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
        Matcher<String> matcher = Matchers.containsString(
                "{\"id\":123,\"name\":\"Name\",\"description\":\"The characteristics of someone or something\",\"imageUrl\":\"https"
                        + "://example.org/example\",\"price\":10.0,\"inStock\":true,\"categoryId\":123}");
        resultActions1.andExpect(MockMvcResultMatchers.content().string(matcher));
    }

    @Test
    public void testFindProductById2() throws Exception {
        when(this.productService.findProductById(or(isA(Long.class), isNull()))).thenReturn(Optional.<Product>empty());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/products/{productId}", 1L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.productController).build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testFindProductsByCategoryName() throws Exception {
        Category category = new Category();
        category.setImageUrl("https://example.org/example");
        category.setId(123L);
        category.setName("Name");
        category.setDescription("The characteristics of someone or something");
        when(this.categoryService.findCategoryByName(or(isA(String.class), isNull())))
                .thenReturn(Optional.<Category>of(category));
        when(this.productService.getAllProducts()).thenReturn(new ArrayList<ProductDto>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/products/category/{categoryName}", "Value");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.productController).build()
                .perform(requestBuilder);
        ResultActions resultActions = actualPerformResult.andExpect(MockMvcResultMatchers.status().isOk());
        ResultActions resultActions1 = resultActions
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
        Matcher<String> matcher = Matchers.containsString("[]");
        resultActions1.andExpect(MockMvcResultMatchers.content().string(matcher));
    }

    @Test
    public void testFindProductsByCategoryName2() throws Exception {
        when(this.categoryService.findCategoryByName(or(isA(String.class), isNull())))
                .thenReturn(Optional.<Category>empty());
        when(this.productService.getAllProducts()).thenReturn(new ArrayList<ProductDto>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/products/category/{categoryName}", "value");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.productController).build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testDownloadImages() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/products/uploaded-images/products/{fileName:.+}",
                "81m7zm.jpg");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.productController).build()
                .perform(requestBuilder);
        ResultActions resultActions = actualPerformResult.andExpect(MockMvcResultMatchers.status().isOk());
        resultActions.andExpect(MockMvcResultMatchers.content().contentType("application/octet-stream"));
    }
}

