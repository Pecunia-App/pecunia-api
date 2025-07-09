package com.pecunia.api.controller;

import com.pecunia.api.dto.UserLoginDTO;
import com.pecunia.api.dto.UserRegistrationDTO;
import com.pecunia.api.model.User;
import com.pecunia.api.security.AuthenticationService;
import com.pecunia.api.security.TokenBlackList;
import com.pecunia.api.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/auth")
public class AuthController {

  private final UserService userService;
  private final AuthenticationService authenticationService;
  private final TokenBlackList tokenBlackList;

  public AuthController(UserService userService, AuthenticationService authenticationService, TokenBlackList tokenBlackList) {
      this.userService = userService;
      this.authenticationService = authenticationService;
      this.tokenBlackList = tokenBlackList;
  }

  @PostMapping("/register")
  public ResponseEntity<User> register(@Valid @RequestBody UserRegistrationDTO userRegistrationDTO) {
    User registeredUser = userService.registerUser(
            userRegistrationDTO.getFirstname(),
            userRegistrationDTO.getLastname(),
            userRegistrationDTO.getEmail(),
            userRegistrationDTO.getPassword(),
            Set.of("ROLE_USER")
    );

    return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/create-user")
  public ResponseEntity<User> createUser(@Valid @RequestBody UserRegistrationDTO userRegistrationDTO) {
    User registeredUser = userService.registerUser(
            userRegistrationDTO.getFirstname(),
            userRegistrationDTO.getLastname(),
            userRegistrationDTO.getEmail(),
            userRegistrationDTO.getPassword(),
            Set.of("ROLE_USER")
    );

    return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
  }

    @PostMapping("/login")
    public ResponseEntity<String> authenticate(@RequestBody UserLoginDTO userLoginDTO) {
        String token = authenticationService.authenticate(
            userLoginDTO.getEmail(),
            userLoginDTO.getPassword()
        );

        return ResponseEntity.ok(token);
    }

  @PostMapping("/logout")
  public ResponseEntity<String> logout(@RequestHeader("Authorization") String authorizationHeader) {
    String token = authorizationHeader.substring(7);
    tokenBlackList.addToBlacklist(token);
    return ResponseEntity.ok("Logout successful");
  }
}
