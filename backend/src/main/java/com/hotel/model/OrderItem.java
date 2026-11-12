package com.hotel.model;
import jakarta.persistence.*;
import java.math.BigDecimal;
@Entity @Table(name="order_items")
public class OrderItem {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @ManyToOne @JoinColumn(name="order_id") public CustomerOrder order;
 @ManyToOne @JoinColumn(name="menu_item_id") public MenuItem menuItem;
 @Column(name="item_name") public String itemName;
 public int quantity;
 @Column(name="unit_price") public BigDecimal unitPrice;
 @Column(name="line_total") public BigDecimal lineTotal;
}
