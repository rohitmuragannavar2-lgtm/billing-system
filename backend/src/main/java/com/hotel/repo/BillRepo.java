package com.hotel.repo;
import com.hotel.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface BillRepo extends JpaRepository<Bill,Long> {
 Optional<Bill> findByOrderId(Long orderId);
 List<Bill> findAllByOrder_User_IdOrderByIdDesc(Long userId);
 List<Bill> findAllByOrderByIdDesc();
}
