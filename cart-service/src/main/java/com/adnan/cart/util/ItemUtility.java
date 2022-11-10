package com.adnan.cart.util;

import com.adnan.cart.model.Product;
import java.math.BigDecimal;

public class ItemUtility {

    public static BigDecimal getSubTotalCostForItem(Product product, int quantity) {
       return (new BigDecimal(product.getPrice())).multiply(BigDecimal.valueOf(quantity));
    }
    
}
