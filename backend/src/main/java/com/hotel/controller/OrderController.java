package com.hotel.controller;
import com.hotel.model.*;
import com.hotel.repo.*;
import com.hotel.service.CurrentUser;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import java.math.*;
import java.util.*;
@RestController @RequestMapping("/api/orders")
public class OrderController {
 public record RequestedItem(Long menuItemId,int quantity){}
 public record PlaceOrder(List<RequestedItem> items){}
 public record StatusInput(String status){}
 private final OrderRepo orders;private final OrderItemRepo items;private final MenuRepo menu;private final CurrentUser current;
 public OrderController(OrderRepo o,OrderItemRepo i,MenuRepo m,CurrentUser c){orders=o;items=i;menu=m;current=c;}
 @PostMapping @Transactional public Map<String,Object> place(@RequestBody PlaceOrder req){
  if(req.items()==null||req.items().isEmpty())throw new IllegalArgumentException("Cart is empty");
  CustomerOrder order=new CustomerOrder();order.user=current.get();order.status="PLACED";order.totalAmount=BigDecimal.ZERO;orders.save(order);
  BigDecimal total=BigDecimal.ZERO;
  for(RequestedItem ri:req.items()){
   if(ri.quantity()<1||ri.quantity()>50)throw new IllegalArgumentException("Invalid quantity");
   MenuItem m=menu.findById(ri.menuItemId()).orElseThrow();if(!m.active)throw new IllegalArgumentException("Menu item unavailable");
   BigDecimal line=m.price.multiply(BigDecimal.valueOf(ri.quantity()));OrderItem oi=new OrderItem();oi.order=order;oi.menuItem=m;oi.itemName=m.name;oi.quantity=ri.quantity();oi.unitPrice=m.price;oi.lineTotal=line;items.save(oi);total=total.add(line);
  }
  order.totalAmount=total;orders.save(order);return detail(order);
 }
 @GetMapping("/my") public List<Map<String,Object>> mine(){return orders.findByUserIdOrderByIdDesc(current.get().id).stream().map(this::detail).toList();}
 @GetMapping public List<Map<String,Object>> all(){return orders.findAllByOrderByIdDesc().stream().map(this::detail).toList();}
 @PatchMapping("/{id}/status") public Map<String,Object> status(@PathVariable Long id,@RequestBody StatusInput in){CustomerOrder o=orders.findById(id).orElseThrow();o.status=in.status();return detail(orders.save(o));}
 private Map<String,Object> detail(CustomerOrder o){
  var lines=items.findByOrderId(o.id).stream().map(i->Map.of("id",i.id,"itemName",i.itemName,"quantity",i.quantity,"unitPrice",i.unitPrice,"lineTotal",i.lineTotal)).toList();
  return Map.of("id",o.id,"customerName",o.user.name,"customerEmail",o.user.email,"status",o.status,"totalAmount",o.totalAmount,"createdAt",o.createdAt==null?"":o.createdAt,"items",lines);
 }
}
