package com.hotel.service;
import com.hotel.model.AppUser;
import com.hotel.repo.UserRepo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
@Service
public class CurrentUser {
 private final UserRepo users;
 public CurrentUser(UserRepo users){this.users=users;}
 public AppUser get(){String email=SecurityContextHolder.getContext().getAuthentication().getName();return users.findByEmail(email).orElseThrow();}
}
