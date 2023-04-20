package com.kuehnenagel.cities.controller;

import com.kuehnenagel.cities.dto.JWTAuthResponse;
import com.kuehnenagel.cities.dto.LoginDto;
import com.kuehnenagel.cities.dto.RegisterDto;
import com.kuehnenagel.cities.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

  private AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  // Build Login REST API
  @PostMapping(value = {"/login", "/signin"})
  public ResponseEntity<JWTAuthResponse> login(@RequestBody LoginDto loginDto) {
    return ResponseEntity.ok(authService.login(loginDto));
  }

  // Build Register REST API
  @PostMapping(value = {"/register", "/signup"})
  public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {
    String response = authService.register(registerDto);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }
}
