package com.parcial2.auth.config;

import com.parcial2.auth.model.Role;
import com.parcial2.auth.model.User;
import com.parcial2.auth.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

  @Bean
  CommandLineRunner createAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    return args -> {
      if (!userRepository.existsByEmail("admin@taxis.com")) {
        User admin = new User();
        admin.setName("Administrador Taxis");
        admin.setEmail("admin@taxis.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRole(Role.ADMIN);
        userRepository.save(admin);
      }
    };
  }
}

