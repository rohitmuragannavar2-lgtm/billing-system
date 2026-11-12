package com.hotel.repo;
import com.hotel.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface OrderItemRepo extends JpaRepository<OrderItem,Long> { List<OrderItem> findByOrderId(Long orderId); }
