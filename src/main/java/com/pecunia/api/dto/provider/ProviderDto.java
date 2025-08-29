package com.pecunia.api.dto.provider;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class ProviderDto {
  private Long id;

  @NotEmpty(message = "Provider name cannot be empty.")
  @Size(min = 1, max = 20, message = "Le nom du fournisseur ne doit pas dépassé 20 caractères.")
  private String providerName;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getProviderName() {
    return providerName;
  }

  public void setProviderName(String providerName) {
    this.providerName = providerName;
  }
}
