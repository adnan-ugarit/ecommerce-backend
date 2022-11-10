package com.adnan.order.model;

import java.math.BigDecimal;

public class CartItem {
    
    private Product product;
    private int quantity;
    private BigDecimal subTotalCost;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getSubTotalCost() {
        return subTotalCost;
    }

    public void setSubTotalCost(BigDecimal subTotalCost) {
        this.subTotalCost = subTotalCost;
    }
    
}
