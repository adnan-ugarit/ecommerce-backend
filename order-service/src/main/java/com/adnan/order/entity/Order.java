package com.adnan.order.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.persistence.*;

@Entity
@Table(name = "orders")
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "tracking_id", unique = true, nullable = false)
    private UUID trackingId;
    
    @Column(name = "customer_address", nullable = false)
    private String customerAddress;
    
    @Column(name = "order_date", nullable = false)
    private Date orderDate;
    
    @Column (name = "total", nullable = false)
    private BigDecimal total;
    
    @OneToMany(cascade = CascadeType.ALL)
    private List<OrderItem> items;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    private Status status;
    
    public enum Status { Pending, Approved, Declined, Delivered }

    public Order() {
    }

    public Order(UUID trackingId, String customerAddress, Date orderDate, BigDecimal total, List<OrderItem> items, Long userId, Status status) {
        this.trackingId = trackingId;
        this.customerAddress = customerAddress;
        this.orderDate = orderDate;
        this.total = total;
        this.items = items;
        this.userId = userId;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(UUID trackingId) {
        this.trackingId = trackingId;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    
}
