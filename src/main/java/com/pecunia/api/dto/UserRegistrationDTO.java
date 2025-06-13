package com.pecunia.api.dto;

import com.pecunia.api.security.PasswordMatches;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@PasswordMatches
public class UserRegistrationDTO {

  @NotBlank(message = "Le prénom est obligatoire.")
  private String firstname;

  @NotBlank(message = "Le nom est obligatoire.")
  private String lastname;

  @Email(message = "Email invalide.")
  private String email;

  @NotBlank(message = "Le mot de passe est obligatoire.")
  @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{12,}$",
  message = "Le mot de passe doit contenir au moins 12 caractères, une majuscule, un chiffre et un caractère spécial.")
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
