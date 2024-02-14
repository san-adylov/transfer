package com.example.app.config;

import com.example.app.repositories.CashboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final CashboxRepository cashboxRepository;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http.authorizeHttpRequests(request -> request
            .requestMatchers("/login", "/actuator/**").permitAll()
            .requestMatchers("/api/v1/cashbox/", "/api/v1/cashbox/**").hasRole("ADMIN")
            .anyRequest().authenticated())
        .formLogin(form -> form
            .successHandler((request, response, authentication) -> {
              Authentication authentication1 = SecurityContextHolder.getContext()
                  .getAuthentication();
              UserDetails userDetails = (UserDetails) authentication1.getPrincipal();
              String role = userDetails.getAuthorities()
                  .stream()
                  .findFirst()
                  .map(GrantedAuthority::getAuthority)
                  .orElse(null);
              assert role != null;
              if (role.equals("ROLE_ADMIN")) {
                response.sendRedirect("/api/v1/cashbox");
              } else {
                response.sendRedirect("/api/v1/transfers");
              }
            })
            .failureUrl("/login?error=true")
            .permitAll())
        .logout(LogoutConfigurer::permitAll)
        .exceptionHandling(exceptionHandling -> exceptionHandling
            .accessDeniedPage("/access-denied"))
        .sessionManagement(sessionManagement -> sessionManagement
            .maximumSessions(1).expiredUrl("/login?expired=true"));

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public UserDetailsService userDetailsService() {
    return new CustomUserDetails(cashboxRepository);
  }

  @Bean
  public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
    AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(
        AuthenticationManagerBuilder.class);
    authenticationManagerBuilder.userDetailsService(userDetailsService())
        .passwordEncoder(passwordEncoder());
    return authenticationManagerBuilder.build();
  }
}
