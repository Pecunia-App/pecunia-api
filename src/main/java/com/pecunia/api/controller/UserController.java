package com.pecunia.api.controller;

import com.pecunia.api.dto.UserDTO;
import com.pecunia.api.dto.UserUpdateDTO;
import com.pecunia.api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "User controller", description = "Handle READ UPDATE and DELETE users")
@RequestMapping("/users")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @Operation(summary = "Returns all users", description = "Role admin require")
  @GetMapping()
  public ResponseEntity<List<UserDTO>> getAllUsers() {
    List<UserDTO> users = userService.getAllUsers();
    return users.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(users);
  }

  @PreAuthorize("#id == authentication.principal.id or hasRole('ROLE_ADMIN')")
  @Operation(
      summary = "Get a specific user by id",
      description = "Role admin require or login with this user id.")
  @GetMapping("/{id}")
  public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
    UserDTO user = userService.getUserById(id);
    return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @Operation(summary = "Get a specific user by firstname", description = "Role admin require.")
  @GetMapping("/search-firstname")
  public ResponseEntity<List<UserDTO>> getUsersByFirstname(@RequestParam String searchTerms) {
    List<UserDTO> users = userService.getUserByFirstname(searchTerms);
    return users.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(users);
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @Operation(summary = "Get a specific user by lastname", description = "Role admin require.")
  @GetMapping("/search-lastname")
  public ResponseEntity<List<UserDTO>> getUsersByLastname(@RequestParam String searchTerms) {
    List<UserDTO> users = userService.getUserByLastname(searchTerms);
    return users.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(users);
  }

  @PreAuthorize("#id == authentication.principal.id or hasRole('ROLE_ADMIN')")
  @Operation(
      summary = "Update a specific user by id",
      description = "Role admin require or login with this user id.")
  @PutMapping("/{id}")
  public ResponseEntity<UserDTO> updateUser(
      @PathVariable Long id, @Valid @RequestBody UserUpdateDTO userDetails) {
    UserDTO updatedUser = userService.updateUser(id, userDetails);
    return updatedUser == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(updatedUser);
  }

  @PreAuthorize("#id == authentication.principal.id or hasRole('ROLE_ADMIN')")
  @Operation(
      summary = "delete a specific user by id",
      description = "role admin require or login with this user id")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    return userService.deleteUserById(id)
        ? ResponseEntity.noContent().build()
        : ResponseEntity.notFound().build();
  }
}
