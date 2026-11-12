package com.hotel.controller;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
@RestController @RequestMapping("/api/admin")
public class AdminMenuController {
 @GetMapping("/health") public Map<String,String> health(){return Map.of("status","ok");}
}
