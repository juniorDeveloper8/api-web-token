package com.robert.seguridad.back.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
  
  private final AutService autService;
  
  @PostMapping(value = "login")
  public ResponseEntity<AuthResponce> login(@RequestBody LoginRequest request){
    return ResponseEntity.ok(autService.login(request));
  }

  @PostMapping(value = "register")
  public ResponseEntity<AuthResponce> register(@RequestBody RegistroRequest request){
    return ResponseEntity.ok(autService.register(request));
  }
}
