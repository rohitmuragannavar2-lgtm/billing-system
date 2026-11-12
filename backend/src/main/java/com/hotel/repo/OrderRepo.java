package com.hotel.repo;
import com.hotel.model.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface OrderRepo extends JpaRepository<CustomerOrder,Long> {
 List<CustomerOrder> findByUserIdOrderByIdDesc(Long userId);
 List<CustomerOrder> findAllByOrderByIdDesc();
}
