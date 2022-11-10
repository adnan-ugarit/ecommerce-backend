package com.adnan.cart.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import com.adnan.cart.model.Product;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

public class ItemUtilityTest {
    @Test
    public void testGetSubTotalCostForItem() {
        Product product = new Product();
        product.setId(123L);
        product.setImageUrl("https://example.org/example");
        product.setName("Name");
        product.setPrice(10.0);
        BigDecimal actualSubTotalCostForItem = ItemUtility.getSubTotalCostForItem(product, 5);
        assertEquals("50", actualSubTotalCostForItem.toString());
    }

    @Test
    public void testGetSubTotalCostForItem2() {
        BigDecimal actualSubTotalCostForItem = ItemUtility.getSubTotalCostForItem(new Product(), 0);
        assertSame(BigDecimal.ZERO, actualSubTotalCostForItem);
    }
}

