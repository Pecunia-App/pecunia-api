package com.pecunia.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PasswordUpdateDTO {

  @JsonProperty("newPassword")
  @NotBlank(message = "Le nouveau mot de passe est obligatoire.")
  @Size(min = 12, max = 128, message = "Le mot de passe doit contenir entre 12 et 128 caractères.")
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
