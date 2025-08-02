package com.pecunia.api.dto.wallet;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

/** WalletCreateDto. */
public class WalletCreateDto {
  @NotBlank(message = "Le nom ne doit pas être vide")
  @Size(min = 2, max = 10, message = "Le nom doit contenir entre 2 et 10 caractères.")
  private String name;

  @NotNull(message = "Le montant de la balance initial ne doit pas être vide.")
  @DecimalMax("2")
  private BigDecimal amount;

  @NotBlank(message = "La devise ne doit pas être vide.")
  @Size(
      min = 3,
      max = 3,
      message = "La devise doit respecter la norme ISO 4217 (ex: USD, EUR, JPY).")
  private String currency;

  private Long userId;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }
}
