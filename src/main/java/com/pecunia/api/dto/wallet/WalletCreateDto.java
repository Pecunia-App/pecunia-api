package com.pecunia.api.dto.wallet;

import com.pecunia.api.model.Money;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** WalletCreateDto. */
public class WalletCreateDto {
  @NotBlank(message = "Le nom ne doit pas être vide")
  @Size(min = 2, max = 10, message = "Le nom doit contenir entre 2 et 10 caractères.")
  private String name;

  @NotNull(message = "Le montant de la balance initial ne doit pas être vide.")
  private Money amount;

  @NotNull(message = "Un Wallet est toujours lié à un utilisateur.")
  private Long userId;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Money getAmount() {
    return amount;
  }

  public void setAmount(Money amount) {
    this.amount = amount;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }
}
