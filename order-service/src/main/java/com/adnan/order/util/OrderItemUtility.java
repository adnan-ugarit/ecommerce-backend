package com.adnan.order.util;

import com.adnan.order.model.Product;
import java.math.BigDecimal;

public class OrderItemUtility {

    public static BigDecimal getSubTotalForOrderItem(Product product, int quantity) {
       return (new BigDecimal(product.getPrice())).multiply(BigDecimal.valueOf(quantity));
    }
    
}
