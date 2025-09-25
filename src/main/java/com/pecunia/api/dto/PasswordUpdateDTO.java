package com.pecunia.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class PasswordUpdateDTO {

  @JsonProperty("newPassword")
  @NotBlank(message = "Le nouveau mot de passe est obligatoire.")
  @Pattern(
      regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+!?=])(?=\\S+$).{12,}$",
      message =
          "Le mot de passe doit contenir au moins 12 caractères, une majuscule, un chiffre et un"
              + " caractère spécial.")
  private String newPassword;

  @JsonProperty("confirmNewPassword")
  @NotBlank(message = "La confirmation du mot de passe est obligatoire.")
  private String confirmNewPassword;

  public PasswordUpdateDTO() {}

  // Vérifie que les deux champs correspondent
  @AssertTrue(message = "Les mots de passe ne correspondent pas.")
  public boolean isPasswordsMatching() {
    return newPassword != null && newPassword.equals(confirmNewPassword);
  }

  public String getNewPassword() {
    return newPassword;
  }

  public void setNewPassword(String newPassword) {
    this.newPassword = newPassword;
  }

  public String getConfirmNewPassword() {
    return confirmNewPassword;
  }

  public void setConfirmNewPassword(String confirmNewPassword) {
    this.confirmNewPassword = confirmNewPassword;
  }
}
