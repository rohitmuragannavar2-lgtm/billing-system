package com.hotel.model;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Entity @Table(name="bills")
public class Bill {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @OneToOne @JoinColumn(name="order_id") public CustomerOrder order;
 @Column(name="bill_number") public String billNumber;
 public BigDecimal subtotal;
 public BigDecimal tax;
 @Column(name="grand_total") public BigDecimal grandTotal;
 @Column(name="generated_at", insertable=false, updatable=false) public LocalDateTime generatedAt;
 @Column(name="emailed_at") public LocalDateTime emailedAt;
}
