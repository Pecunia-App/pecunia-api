package com.pecunia.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/** BaseEntity. */
@MappedSuperclass
public class BaseEntity {

  @Column(name = "created_at", nullable = false, updatable = true)
  @CreationTimestamp
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false, updatable = true)
  @UpdateTimestamp
  private LocalDateTime updatedAt;

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
}
