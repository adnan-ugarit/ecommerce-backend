package com.adnan.cart.controller;

import static org.mockito.AdditionalMatchers.or;
import static org.mockito.Mockito.isA;
import static org.mockito.Mockito.isNull;
import static org.mockito.Mockito.when;

import com.adnan.cart.model.Cart;
import com.adnan.cart.model.Item;
import com.adnan.cart.service.CartService;

import java.math.BigDecimal;
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

@ContextConfiguration(classes = {CartController.class})
@ExtendWith(SpringExtension.class)
public class CartControllerTest {
    @Autowired
    private CartController cartController;

    @MockBean
    private CartService cartService;

    @Test
    public void testGetCart() throws Exception {
        String cartId = "";
        BigDecimal subTotalCost = BigDecimal.valueOf(50);
        Item item = new Item();
        item.setSubTotalCost(subTotalCost);
        ArrayList<Item> items = new ArrayList<>();
        items.add(item);
        BigDecimal totalCost = BigDecimal.valueOf(50);
        Cart cart = new Cart(cartId, items, totalCost);
        when(this.cartService.getCart(or(isA(String.class), isNull()))).thenReturn(cart);
    }

    @Test
    public void testAddProductToCart() throws Exception {
        MockHttpServletRequestBuilder result = MockMvcRequestBuilders.post("/add");
        MockHttpServletRequestBuilder paramResult = result.param("productId", String.valueOf(1L));
        MockHttpServletRequestBuilder requestBuilder = paramResult.param("quantity", String.valueOf(5));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.cartController).build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testDeleteItem() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/delete/{itemId}", 1L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.cartController).build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testClearCart() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/clear");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.cartController).build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testFetchCartItems() throws Exception {
        BigDecimal totalCost = BigDecimal.valueOf(1L);
        when(this.cartService.getCart(or(isA(String.class), isNull())))
                .thenReturn(new Cart("42", new ArrayList<Item>(), totalCost));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/cart/fetch").header("cookie", "Cookie");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.cartController).build()
                .perform(requestBuilder);
        ResultActions resultActions = actualPerformResult.andExpect(MockMvcResultMatchers.status().isOk());
        ResultActions resultActions1 = resultActions
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
        Matcher<String> matcher = Matchers.containsString("[]");
        resultActions1.andExpect(MockMvcResultMatchers.content().string(matcher));
    }
}

