package com.pecunia.api.dto;

import com.pecunia.api.security.PasswordMatches;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@PasswordMatches
@Schema(name = "User Registration DTO", description = "Use password matcher and a regex")
public class UserRegistrationDTO {

  @Pattern(
      regexp = "^(?=.*[A-Za-zÀ-ÿ])[A-Za-zÀ-ÿ\\-]+$",
      message =
          "Le prénom doit contenir uniquement des lettres (avec accents) et des tirets, sans"
              + " chiffres ni espaces.")
  @Size(min = 2, max = 50, message = "Le prénom doit contenir entre 2 et 50 caractères")
  private String firstname;

  @Pattern(
      regexp = "^(?=.*[A-Za-zÀ-ÿ])[A-Za-zÀ-ÿ\\-]+$",
      message =
          "Le prénom doit contenir uniquement des lettres (avec accents) et des tirets, sans"
              + " chiffres ni espaces.")
  @Size(min = 2, max = 50, message = "Le prénom doit contenir entre 2 et 50 caractères")
  private String lastname;

  @Email(message = "Email invalide.")
  private String email;

  @NotBlank(message = "Le mot de passe est obligatoire.")
  @Pattern(
      regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+!?=])(?=\\S+$).{12,}$",
      message =
          "Le mot de passe doit contenir au moins 12 caractères, une majuscule, un chiffre et un"
              + " caractère spécial.")
  private String password;

  @NotBlank(message = "Veuillez confirmer votre mot de passe.")
  private String confirmPassword;

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

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getConfirmPassword() {
    return confirmPassword;
  }

  public void setConfirmPassword(String confirmPassword) {
    this.confirmPassword = confirmPassword;
  }
}
