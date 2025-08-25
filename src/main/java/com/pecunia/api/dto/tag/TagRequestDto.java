package com.pecunia.api.dto.tag;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** TagUpdateCreateDto. */
public class TagRequestDto {
  @NotBlank(message = "Le nom du tag est obligatoire.")
  @Size(min = 1, max = 20, message = "L'étiquette peut contenir un maximum de 20 caractères.")
  private String tagName;

  public String getTagName() {
    return tagName;
  }

  public void setTagName(String tagName) {
    this.tagName = tagName;
  }
}
