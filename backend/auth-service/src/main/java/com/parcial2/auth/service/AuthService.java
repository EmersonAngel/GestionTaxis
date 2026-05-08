package com.parcial2.auth.service;

import com.parcial2.auth.dto.AuthResponse;
import com.parcial2.auth.dto.LoginRequest;
import com.parcial2.auth.dto.RegisterRequest;
import com.parcial2.auth.dto.UserResponse;
import com.parcial2.auth.model.Role;
import com.parcial2.auth.model.User;
import com.parcial2.auth.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;

  public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
  }

  public AuthResponse register(RegisterRequest request) {
    if (userRepository.existsByEmail(request.email())) {
      throw new IllegalArgumentException("El correo ya esta registrado");
    }

    User user = new User();
    user.setName(request.name());
    user.setEmail(request.email().toLowerCase());
    user.setPassword(passwordEncoder.encode(request.password()));
    user.setRole(request.role() == null ? Role.USER : request.role());

    User saved = userRepository.save(user);
    return new AuthResponse(jwtService.generateToken(saved), toResponse(saved));
  }

  public AuthResponse login(LoginRequest request) {
    User user = userRepository.findByEmail(request.email().toLowerCase())
        .orElseThrow(() -> new BadCredentialsException("Credenciales invalidas"));

    if (!passwordEncoder.matches(request.password(), user.getPassword())) {
      throw new BadCredentialsException("Credenciales invalidas");
    }

    return new AuthResponse(jwtService.generateToken(user), toResponse(user));
  }

  public UserResponse findByEmail(String email) {
    return userRepository.findByEmail(email)
        .map(this::toResponse)
        .orElseThrow(() -> new BadCredentialsException("Usuario no encontrado"));
  }

  private UserResponse toResponse(User user) {
    return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getRole());
  }
}

