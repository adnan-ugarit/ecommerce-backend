package com.adnan.order.entity;

import java.math.BigDecimal;
import javax.persistence.*;

@Entity
@Table(name = "order_items")
public class OrderItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "product_id", nullable = false)
    private Long productId;
    
    @Column(nullable = false)
    private int quantity;
    
    @Column(name = "product_price")
    private double productPrice;
    
    @Column(name = "product_name")
    private String productName;
    
    @Column (name = "sub_total", nullable = false)
    private BigDecimal subTotal;

    public OrderItem() {
    }

    public OrderItem(Long productId, int quantity, double productPrice, String productName, BigDecimal subTotal) {
        this.productId = productId;
        this.quantity = quantity;
        this.productPrice = productPrice;
        this.productName = productName;
        this.subTotal = subTotal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }
    
}
