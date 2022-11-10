package com.adnan.cart.util;

import com.adnan.cart.model.Item;
import java.math.BigDecimal;
import java.util.List;

public class CartUtility {

    public static BigDecimal getTotalCostForCart(List<Item> items) {
        BigDecimal total = BigDecimal.ZERO;
        for (int i = 0; i < items.size(); i++) {
            total = total.add(items.get(i).getSubTotalCost());
        }
        return total;
    }
    
}
