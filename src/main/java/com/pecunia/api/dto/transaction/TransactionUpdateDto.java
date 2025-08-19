package com.pecunia.api.dto.transaction;

import com.pecunia.api.model.Money;
import com.pecunia.api.model.TransactionType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** TransactionUpdateDto. */
public class TransactionUpdateDto {
  @NotNull(message = "Le montant ne doit pas être vide.")
  private Money amount;

  private TransactionType type;

  @Size(min = 0, max = 20, message = "La note peut contenir un maximum de 20 caractères.")
  private String note;

  public Money getAmount() {
    return amount;
  }

  public void setAmount(Money amount) {
    this.amount = amount;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  public TransactionType getType() {
    return type;
  }

  public void setType(TransactionType type) {
    this.type = type;
  }
}
