package com.hotel.service;
import com.hotel.model.AppUser;
import com.hotel.repo.UserRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
@Component
public class AdminSeeder implements CommandLineRunner {
 private final UserRepo users; private final PasswordEncoder encoder;
 @Value("${app.admin.email}") String email; @Value("${app.admin.password}") String password;
 public AdminSeeder(UserRepo users,PasswordEncoder encoder){this.users=users;this.encoder=encoder;}
 public void run(String... args){
  if(users.findByEmail(email).isEmpty()){AppUser u=new AppUser();u.name="Hotel Administrator";u.email=email;u.password=encoder.encode(password);u.role="ADMIN";users.save(u);}
 }
}
