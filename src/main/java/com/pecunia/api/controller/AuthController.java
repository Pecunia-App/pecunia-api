package com.pecunia.api.controller;

import com.pecunia.api.dto.UserRegistrationDTO;
import com.pecunia.api.model.User;
import com.pecunia.api.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/auth")
public class AuthController {
  private final UserService userService;

  public AuthController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/register")
  public ResponseEntity<User> register(@RequestBody UserRegistrationDTO userRegistrationDTO) {
    User registeredUser = userService.registerUser(
            userRegistrationDTO.getEmail(),
            userRegistrationDTO.getPassword(),
            Set.of("ROLE_USER")
    );

    return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
  }
}
