package com.pecunia.api.dto.transaction;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pecunia.api.model.Money;
import com.pecunia.api.model.TransactionType;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Set;

/** TransactionUpdateDto. */
public class TransactionUpdateDto {
  private Money amount;

  private TransactionType type;

  @Size(min = 0, max = 20, message = "La note peut contenir un maximum de 20 caractères.")
  private String note;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime createdAt;

  private Set<Long> tagsIds;

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

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

  public Set<Long> getTagsIds() {
    return tagsIds;
  }

  public void setTagsIds(Set<Long> tagsIds) {
    this.tagsIds = tagsIds;
  }
}
