package com.adnan.cart.model;

import java.math.BigDecimal;

public class Item {
    
    private Product product;
    private int quantity;
    private BigDecimal subTotalCost;

    public Item() {
    }

    public Item(Product product, int quantity, BigDecimal subTotalCost) {
        this.product = product;
        this.quantity = quantity;
        this.subTotalCost = subTotalCost;
    }

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
