package com.hotel.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity @Table(name="users")
public class AppUser {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 public String name;
 @Column(nullable=false, unique=true) public String email;
 public String phone;
 @Column(nullable=false) public String password;
 @Column(nullable=false) public String role;
 @Column(name="created_at", insertable=false, updatable=false) public LocalDateTime createdAt;
}
