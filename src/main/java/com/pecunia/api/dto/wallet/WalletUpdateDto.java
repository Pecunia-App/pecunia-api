package com.pecunia.api.dto.wallet;

import com.pecunia.api.model.Money;
import jakarta.validation.constraints.Size;

/** WalletUpdateDto. */
public class WalletUpdateDto {

  @Size(min = 2, max = 10, message = "Le nom doit contenir entre 2 et 10 caractères.")
  private String name;

  private Money amount;

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
}
