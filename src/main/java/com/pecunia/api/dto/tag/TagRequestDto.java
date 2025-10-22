package com.pecunia.api.dto.tag;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** TagUpdateCreateDto. */
public class TagRequestDto {
  @NotBlank(message = "Le nom du tag est obligatoire.")
  @Size(min = 1, max = 20, message = "L'étiquette peut contenir un maximum de 20 caractères.")
  private String tagName;

  @NotNull(message = "Une étiquette est toujours lié à un utilisateur")
  private Long userId;

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getTagName() {
    return tagName;
  }

  public void setTagName(String tagName) {
    this.tagName = tagName;
  }
}
