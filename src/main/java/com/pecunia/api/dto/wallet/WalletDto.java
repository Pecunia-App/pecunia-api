package com.pecunia.api.dto.wallet;

import com.pecunia.api.model.Money;

/** WalletDto. */
public class WalletDto {
  private Long id;
  private String name;
  private Money amount;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

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
