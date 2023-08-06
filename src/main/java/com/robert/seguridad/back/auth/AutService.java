package com.robert.seguridad.back.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.robert.seguridad.back.Interface.UserRepository;
import com.robert.seguridad.back.jwt.JwtService;
import com.robert.seguridad.back.models.Role;
import com.robert.seguridad.back.models.User;

//import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AutService {
  
  private final UserRepository userRepository;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final PasswordEncoder passwordEncoder;

  public AuthResponce login(LoginRequest request) {
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
    UserDetails user=userRepository.findByUsername(request.getUsername()).orElseThrow();
    String token=jwtService.getToken(user);
    return AuthResponce.builder()
      .token(token)
      .build();
  }

  public AuthResponce register(RegistroRequest request) {
    User user = User.builder()
      .username(request.getUsername())
      .password(passwordEncoder.encode( request.getPassword()))
      .firstname(request.getFirstname())
      .lastname(request.getLastname())
      .role(Role.USER)
      .build();

      userRepository.save(user);

      return AuthResponce.builder()
        .token(jwtService.getToken(user))
        .build();
  }

}
