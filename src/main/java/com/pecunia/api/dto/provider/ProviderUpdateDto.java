package com.pecunia.api.dto.provider;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ProviderUpdateDto {
  @NotBlank(message = "Le nom du fournisseur est obligatoire.")
  @Size(min = 1, max = 20, message = "Le nom du fournisseur ne doit pas dépasser 20 caractères.")
  private String providerName;

  public String getProviderName() {
    return providerName;
  }

  public void setProviderName(String providerName) {
    this.providerName = providerName;
  }
}
