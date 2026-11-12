package com.hotel.controller;
import com.hotel.model.MenuItem;
import com.hotel.repo.MenuRepo;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
@RestController @RequestMapping("/api/menu")
public class MenuController {
 public record MenuInput(String name,String description,String category,BigDecimal price,boolean vegetarian){}
 private final MenuRepo repo;public MenuController(MenuRepo r){repo=r;}
 @GetMapping public List<MenuItem> all(){return repo.findByActiveTrueOrderByCategoryAscNameAsc();}
 @PostMapping public MenuItem create(@RequestBody MenuInput i){MenuItem m=from(i);return repo.save(m);}
 @PutMapping("/{id}") public MenuItem update(@PathVariable Long id,@RequestBody MenuInput i){MenuItem m=repo.findById(id).orElseThrow();m.name=i.name();m.description=i.description();m.category=i.category();m.price=i.price();m.vegetarian=i.vegetarian();return repo.save(m);}
 @DeleteMapping("/{id}") public void delete(@PathVariable Long id){MenuItem m=repo.findById(id).orElseThrow();m.active=false;repo.save(m);}
 private MenuItem from(MenuInput i){MenuItem m=new MenuItem();m.name=i.name();m.description=i.description();m.category=i.category();m.price=i.price();m.vegetarian=i.vegetarian();return m;}
}
