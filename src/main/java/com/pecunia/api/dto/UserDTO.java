package com.pecunia.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "User DTO", description = "Represents a user with public information")
public class UserDTO {

  private Long id;

  @NotBlank(message = "Le nom est obligatoire.")
  private String firstname;

  @NotBlank(message = "Le nom est obligatoire.")
  private String lastname;

  @NotBlank(message = "Le nom est obligatoire.")
  private String profilePicture;

  @Email(message = "Email invalide.")
  @NotBlank(message = "Le nom est obligatoire.")
  private String email;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFirstname() {
    return firstname;
  }

  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  public String getProfilePicture() {
    return profilePicture;
  }

  public void setProfilePicture(String profilePicture) {
    this.profilePicture = profilePicture;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
