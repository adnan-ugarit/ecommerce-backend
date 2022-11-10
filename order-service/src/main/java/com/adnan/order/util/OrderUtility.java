package com.adnan.order.util;

import com.adnan.order.entity.OrderItem;
import java.math.BigDecimal;
import java.util.List;

public class OrderUtility {

    public static BigDecimal getTotalForOrder(List<OrderItem> orderItems) {
        BigDecimal total = BigDecimal.ZERO;
        for (int i = 0; i < orderItems.size(); i++) {
            total = total.add(orderItems.get(i).getSubTotal());
        }
        return total;
    }
    
}
