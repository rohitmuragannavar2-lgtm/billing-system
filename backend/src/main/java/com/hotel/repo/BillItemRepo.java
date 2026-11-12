package com.hotel.repo;
import com.hotel.model.BillItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface BillItemRepo extends JpaRepository<BillItem,Long> { List<BillItem> findByBillId(Long billId); }
