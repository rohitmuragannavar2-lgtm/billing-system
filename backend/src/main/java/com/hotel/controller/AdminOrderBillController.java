package com.hotel.controller;
import com.hotel.model.*;
import com.hotel.repo.*;
import com.hotel.controller.BillController;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
@RestController
public class AdminOrderBillController {
 private final BillController bills;
 public AdminOrderBillController(BillController b){bills=b;}
 @PostMapping("/api/orders/{orderId}/generate-bill") public Map<String,Object> generate(@PathVariable Long orderId){return bills.generateForAdmin(orderId);}
}
