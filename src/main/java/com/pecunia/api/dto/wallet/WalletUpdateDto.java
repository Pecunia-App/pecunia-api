package com.pecunia.api.dto.wallet;

import com.pecunia.api.model.Money;

/** WalletUpdateDto. */
public class WalletUpdateDto {
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
