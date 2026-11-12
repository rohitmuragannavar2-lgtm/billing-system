package com.hotel.model;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Entity @Table(name="customer_orders")
public class CustomerOrder {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @ManyToOne @JoinColumn(name="user_id") public AppUser user;
 public String status="PLACED";
 @Column(name="total_amount") public BigDecimal totalAmount;
 @Column(name="created_at", insertable=false, updatable=false) public LocalDateTime createdAt;
}
