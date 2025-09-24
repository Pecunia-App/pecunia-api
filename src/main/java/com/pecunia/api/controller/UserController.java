package com.pecunia.api.controller;

import com.pecunia.api.dto.UserDTO;
import com.pecunia.api.dto.UserUpdateDTO;
import com.pecunia.api.model.User;
import com.pecunia.api.security.HasRole;
import com.pecunia.api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

@RestController
@Tag(name = "User controller", description = "Handle READ UPDATE and DELETE users")
@RequestMapping("/users")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @Operation(summary = "Returns all users", description = "Role admin require")
  @GetMapping()
  @HasRole("ADMIN")
  public ResponseEntity<List<UserDTO>> getAllUsers() {
    List<UserDTO> users = userService.getAllUsers();
    return users.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(users);
  }

  @Operation(
      summary = "Get a specific user by id",
      description = "Role admin require or login with this user id.")
  @GetMapping("/{id}")
  @PreAuthorize("#id == authentication.principal.id or hasRole('ROLE_ADMIN')")
  public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
    UserDTO user = userService.getUserById(id);
    return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
  }

  @GetMapping("/me")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<UserDTO> getCurrentUser(Authentication authentication) {
    User user = (User) authentication.getPrincipal();
    Long userId = user.getId();

    UserDTO userDTO = userService.getUserById(userId);
    return ResponseEntity.ok(userDTO);
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @Operation(summary = "Get a specific user by firstname", description = "Role admin require.")
  @GetMapping("/search-firstname")
  @HasRole("ADMIN")
  public ResponseEntity<List<UserDTO>> getUsersByFirstname(@RequestParam String searchTerms) {
    List<UserDTO> users = userService.getUserByFirstname(searchTerms);
    return users.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(users);
  }

  @Operation(summary = "Get a specific user by lastname", description = "Role admin require.")
  @GetMapping("/search-lastname")
  @HasRole("ADMIN")
  public ResponseEntity<List<UserDTO>> getUsersByLastname(@RequestParam String searchTerms) {
    List<UserDTO> users = userService.getUserByLastname(searchTerms);
    return users.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(users);
  }

  @Operation(
      summary = "Update a specific user by id",
      description = "Role admin require or login with this user id.")
  @PutMapping("/{id}")
  @PreAuthorize("#id == authentication.principal.id or hasRole('ROLE_ADMIN')")
  public ResponseEntity<UserDTO> updateUser(
      @PathVariable Long id, @Valid @RequestBody UserUpdateDTO userDetails) {
    UserDTO updatedUser = userService.updateUser(id, userDetails);
    return updatedUser == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(updatedUser);
  }

  @PreAuthorize("#id == authentication.principal.id or hasRole('ROLE_ADMIN')")
  @PutMapping("/{id}/password")
  public ResponseEntity<Void> updatePassword(
      @PathVariable Long id, @Valid @RequestBody com.pecunia.api.dto.PasswordUpdateDTO body) {

    // Délègue au service: il chargera l’utilisateur, encodera avec BCrypt, et sauvegardera
    userService.updatePassword(id, body.getNewPassword());

    return ResponseEntity.noContent().build(); // 204 si OK
  }

  @Operation(
      summary = "delete a specific user by id",
      description = "role admin require or login with this user id")
  @DeleteMapping("/{id}")
  @PreAuthorize("#id == authentication.principal.id or hasRole('ROLE_ADMIN')")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    return userService.deleteUserById(id)
        ? ResponseEntity.noContent().build()
        : ResponseEntity.notFound().build();
  }
}
