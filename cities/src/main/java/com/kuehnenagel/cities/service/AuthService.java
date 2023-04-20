package com.kuehnenagel.cities.service;

import com.kuehnenagel.cities.dto.JWTAuthResponse;
import com.kuehnenagel.cities.dto.LoginDto;
import com.kuehnenagel.cities.dto.RegisterDto;
import com.kuehnenagel.cities.entity.User;

public interface AuthService {
  JWTAuthResponse login(LoginDto loginDto);

  String register(RegisterDto registerDto);

  User getCurrentUser();
}
