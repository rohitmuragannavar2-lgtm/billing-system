package com.hotel.repo;
import com.hotel.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface MenuRepo extends JpaRepository<MenuItem,Long> { List<MenuItem> findByActiveTrueOrderByCategoryAscNameAsc(); }
