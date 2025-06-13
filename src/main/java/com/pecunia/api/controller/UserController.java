package com.pecunia.api.controller;

import com.pecunia.api.dto.UserDTO;
import com.pecunia.api.model.User;
import com.pecunia.api.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping()
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(users);
    }

    @PreAuthorize("#id == authentication.principal.id or hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(user);
    }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @GetMapping("/search-firstname")
  public ResponseEntity<List<UserDTO>> getUsersByFirstname(@RequestParam String searchTerms) {
    List<UserDTO> users = userService.getUserByFirstname(searchTerms);
    if (users.isEmpty()) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok(users);
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @GetMapping("/search-lastname")
  public ResponseEntity<List<UserDTO>> getUsersByLastname(@RequestParam String searchTerms) {
    List<UserDTO> users = userService.getUserByLastname(searchTerms);
    if (users.isEmpty()) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok(users);
  }

    @PreAuthorize("#id == authentication.principal.id or hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @Valid @RequestBody User userDetails) {
        UserDTO updatedUser = userService.updateUser(id, userDetails);
        System.out.println("User details" + userDetails);
        System.out.println("Update user" + updatedUser);
        if (updatedUser == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedUser);
    }

    @PreAuthorize("#id == authentication.principal.id or hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
      if (userService.deleteUserById(id)) {
        return ResponseEntity.noContent().build();
      } else {
        return ResponseEntity.notFound().build();
      }
    }
}
