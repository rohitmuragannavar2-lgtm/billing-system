package com.hotel.config;
import com.hotel.security.JwtFilter;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.*;
import java.util.List;
@Configuration
public class SecurityConfig {
 @Bean PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}
 @Bean SecurityFilterChain filterChain(HttpSecurity http,JwtFilter jwtFilter)throws Exception{
  return http.csrf(c->c.disable()).cors(c->{}).sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
   .authorizeHttpRequests(a->a.requestMatchers("/api/auth/**","/error").permitAll()
    .requestMatchers(HttpMethod.GET,"/api/menu/**").permitAll()
    .requestMatchers("/api/menu/**","/api/admin/**").hasRole("ADMIN")
    .requestMatchers(HttpMethod.POST,"/api/orders").hasRole("CUSTOMER")
    .requestMatchers(HttpMethod.GET,"/api/orders/my","/api/bills/my").hasRole("CUSTOMER")
    .requestMatchers(HttpMethod.GET,"/api/orders","/api/bills").hasRole("ADMIN")
    .requestMatchers(HttpMethod.PATCH,"/api/orders/*/status").hasRole("ADMIN")
    .requestMatchers(HttpMethod.POST,"/api/orders/*/generate-bill","/api/bills/*/email").hasRole("ADMIN")
    .requestMatchers("/api/orders/**","/api/bills/**").authenticated()
    .anyRequest().authenticated())
   .addFilterBefore(jwtFilter,UsernamePasswordAuthenticationFilter.class).build();
 }
 @Bean CorsConfigurationSource corsConfigurationSource(){
  CorsConfiguration c=new CorsConfiguration(); c.setAllowedOrigins(List.of("http://localhost:5173")); c.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS")); c.setAllowedHeaders(List.of("*")); c.setAllowCredentials(true);
  UrlBasedCorsConfigurationSource s=new UrlBasedCorsConfigurationSource();s.registerCorsConfiguration("/**",c);return s;
 }
}
