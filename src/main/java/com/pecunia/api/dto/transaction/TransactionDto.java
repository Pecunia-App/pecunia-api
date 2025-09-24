package com.pecunia.api.dto.transaction;

import com.pecunia.api.dto.provider.ProviderDto;
import com.pecunia.api.dto.tag.TagDto;
import com.pecunia.api.model.CategoryType;
import com.pecunia.api.model.Money;
import java.time.LocalDateTime;
import java.util.Set;

/** TransactionDto. */
public class TransactionDto {
  private Long id;
  private CategoryType type;
  private String note;
  private Money amount;
  private Set<TagDto> tags;
  private ProviderDto provider;

  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public CategoryType getType() {
    return type;
  }

  public void setType(CategoryType type) {
    this.type = type;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  public Money getAmount() {
    return amount;
  }

  public void setAmount(Money amount) {
    this.amount = amount;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public Set<TagDto> getTags() {
    return tags;
  }

  public void setTags(Set<TagDto> tags) {
    this.tags = tags;
  }

  public ProviderDto getProvider() {
    return provider;
  }

  public void setProvider(ProviderDto provider) {
    this.provider = provider;
  }
}
