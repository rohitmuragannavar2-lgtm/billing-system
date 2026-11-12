package com.hotel.model;
import jakarta.persistence.*;
import java.math.BigDecimal;
@Entity @Table(name="menu_items")
public class MenuItem {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 public String name;
 public String description;
 public String category;
 public BigDecimal price;
 public boolean vegetarian;
 public boolean active=true;
}
