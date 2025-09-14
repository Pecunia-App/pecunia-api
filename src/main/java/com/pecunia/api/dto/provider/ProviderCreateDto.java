package com.pecunia.api.dto.provider;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ProviderCreateDto {
  @NotBlank(message = "Le nom du fournisseur est obligatoire.")
  @Size(min = 1, max = 20, message = "Le nom du fournisseur ne doit pas dépasser 20 caractères.")
  private String providerName;

  @NotNull(message = "Un Fournisseur est toujours lié à un utilisateur.")
  private Long userId;

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getProviderName() {
    return providerName;
  }

  public void setProviderName(String providerName) {
    this.providerName = providerName;
  }
}
