package com.adnan.cart.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import com.adnan.cart.model.Item;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class CartUtilityTest {
    @Test
    public void testGetTotalCostForCart() {
        BigDecimal actualTotalCostForCart = CartUtility.getTotalCostForCart(new ArrayList<Item>());
        assertSame(BigDecimal.ZERO, actualTotalCostForCart);
    }

    @Test
    public void testGetTotalCostForCart2() {
        BigDecimal subTotalCost = BigDecimal.valueOf(50);
        Item item = new Item();
        item.setSubTotalCost(subTotalCost);
        ArrayList<Item> itemList = new ArrayList<>();
        itemList.add(item);
        BigDecimal actualTotalCostForCart = CartUtility.getTotalCostForCart(itemList);
        assertEquals("50", actualTotalCostForCart.toString());
    }
}

