package com.pecunia.api.dto.wallet;

import java.math.BigDecimal;

/** WalletUpdateDto. */
public class WalletUpdateDto {
  private String name;
  private BigDecimal amount;
  private String currency;

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
}
