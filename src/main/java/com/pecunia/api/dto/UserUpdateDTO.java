package com.pecunia.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "User Update DTO", description = "Represents a user for update")
public class UserUpdateDTO {

  @NotBlank(message = "Le prénom est obligatoire.")
  private String firstname;

  @NotBlank(message = "Le nom est obligatoire.")
  private String lastname;

  @NotBlank(message = "La photo est obligatoire.")
  private String profilePicture;

  @Email(message = "Email invalide.")
  @NotBlank(message = "L'email est obligatoire.")
  private String email;

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
