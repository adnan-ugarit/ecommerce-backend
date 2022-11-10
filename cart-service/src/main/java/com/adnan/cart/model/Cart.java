package com.adnan.cart.model;

import java.math.BigDecimal;
import java.util.List;

public class Cart {
    
    private String id;
    private List<Item> items;
    private BigDecimal totalCost;

    public Cart(String id, List<Item> items, BigDecimal totalCost) {
        this.id = id;
        this.items = items;
        this.totalCost = totalCost;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }
    
}
