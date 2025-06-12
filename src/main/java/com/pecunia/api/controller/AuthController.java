package com.pecunia.api.controller;

import com.pecunia.api.dto.UserLoginDTO;
import com.pecunia.api.dto.UserRegistrationDTO;
import com.pecunia.api.model.User;
import com.pecunia.api.security.AuthenticationService;
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
  private final AuthenticationService authenticationService;

  public AuthController(UserService userService, AuthenticationService authenticationService) {
      this.userService = userService;
      this.authenticationService = authenticationService;
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

    @PostMapping("/login")
    public ResponseEntity<String> authenticate(@RequestBody UserLoginDTO userLoginDTO) {
        String token = authenticationService.authenticate(
            userLoginDTO.getEmail(),
            userLoginDTO.getPassword()
        );

        return ResponseEntity.ok(token);
    }
}
