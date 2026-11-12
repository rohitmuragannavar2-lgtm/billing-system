package com.hotel.model;
import jakarta.persistence.*;
import java.math.BigDecimal;
@Entity @Table(name="bill_items")
public class BillItem {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @ManyToOne @JoinColumn(name="bill_id") public Bill bill;
 @Column(name="item_name") public String itemName;
 public int quantity;
 @Column(name="unit_price") public BigDecimal unitPrice;
 @Column(name="line_total") public BigDecimal lineTotal;
}
