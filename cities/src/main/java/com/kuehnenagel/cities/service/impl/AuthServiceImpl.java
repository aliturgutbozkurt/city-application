package com.kuehnenagel.cities.service.impl;

import com.kuehnenagel.cities.dto.JWTAuthResponse;
import com.kuehnenagel.cities.dto.LoginDto;
import com.kuehnenagel.cities.dto.RegisterDto;
import com.kuehnenagel.cities.entity.Role;
import com.kuehnenagel.cities.entity.User;
import com.kuehnenagel.cities.exception.ApplicationException;
import com.kuehnenagel.cities.repository.RoleRepository;
import com.kuehnenagel.cities.repository.UserRepository;
import com.kuehnenagel.cities.security.JwtTokenProvider;
import com.kuehnenagel.cities.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final AuthenticationManager authenticationManager;
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider jwtTokenProvider;

  @Override
  public JWTAuthResponse login(LoginDto loginDto) {

    var authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(), loginDto.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
    jwtAuthResponse.setAccessToken(jwtTokenProvider.generateToken(authentication));
    return jwtAuthResponse;
  }

  @Override
  public String register(RegisterDto registerDto) {

    // add check for username exists in database
    if (userRepository.existsByUsername(registerDto.getUsername())) {
      throw new ApplicationException(HttpStatus.BAD_REQUEST, "Username is already exists!.");
    }

    var user =
        User.builder()
            .fullName(registerDto.getFullName())
            .username(registerDto.getUsername())
            .password(passwordEncoder.encode(registerDto.getPassword()))
            .build();

    Set<Role> roles = new HashSet<>();
    var userRole = roleRepository.findByName("ROLE_USER").get();
    roles.add(userRole);
    var userRole2 = roleRepository.findByName("ROLE_ALLOW_EDIT").get();
    roles.add(userRole2);
    user.setRoles(roles);

    userRepository.save(user);

    return "User registered successfully!.";
  }

  @Override
  public User getCurrentUser() {
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    return userRepository
        .findByUsername(authentication.getName())
        .orElseThrow(
            () ->
                new ApplicationException(HttpStatus.UNAUTHORIZED, "User not found in the context"));
  }
}
