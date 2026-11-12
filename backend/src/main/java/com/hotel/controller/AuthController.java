package com.hotel.controller;
import com.hotel.model.AppUser;
import com.hotel.repo.UserRepo;
import com.hotel.security.JwtService;
import jakarta.validation.constraints.*;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
@RestController @RequestMapping("/api/auth")
public class AuthController {
 public record Register(@NotBlank String name,@Email @NotBlank String email,@NotBlank String phone,@Size(min=6) String password){}
 public record Login(@Email @NotBlank String email,@NotBlank String password){}
 private final UserRepo users;private final PasswordEncoder encoder;private final JwtService jwt;
 public AuthController(UserRepo u,PasswordEncoder e,JwtService j){users=u;encoder=e;jwt=j;}
 @PostMapping("/register") public ResponseEntity<?> register(@RequestBody Register r){
  if(users.findByEmail(r.email().toLowerCase()).isPresent())return ResponseEntity.badRequest().body(Map.of("message","Email already registered"));
  AppUser u=new AppUser();u.name=r.name();u.email=r.email().toLowerCase();u.phone=r.phone();u.password=encoder.encode(r.password());u.role="CUSTOMER";users.save(u);
  return ResponseEntity.ok(Map.of("token",jwt.generate(u.email,u.role),"role",u.role,"name",u.name,"email",u.email));
 }
 @PostMapping("/login") public ResponseEntity<?> login(@RequestBody Login r){
  var u=users.findByEmail(r.email().toLowerCase()).orElse(null);
  if(u==null||!encoder.matches(r.password(),u.password))return ResponseEntity.status(401).body(Map.of("message","Invalid email or password"));
  return ResponseEntity.ok(Map.of("token",jwt.generate(u.email,u.role),"role",u.role,"name",u.name,"email",u.email));
 }
}
