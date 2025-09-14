package com.pecunia.api.dto.category;

import com.pecunia.api.model.CategoryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CategoryCreateDto {
  @NotBlank(message = "Le nom de la catégorie est obligatoire.")
  @Size(min = 1, max = 10, message = "Le nom de la catégorie ne doit pas dépasser 10 caractères.")
  private String categoryName;

  private String icon;

  @Pattern(
      regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$",
      message = "La couleur doit être au format hexadécimal (ex: #FF5733 ou #F53)")
  private String color;

  private Boolean isGlobal;
  private CategoryType type;
  private Long userId;

  public String getCategoryName() {
    return categoryName;
  }

  public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
  }

  public String getIcon() {
    return icon;
  }

  public void setIcon(String icon) {
    this.icon = icon;
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public Boolean getIsGlobal() {
    return isGlobal;
  }

  public void setIsGlobal(Boolean isGlobal) {
    this.isGlobal = isGlobal;
  }

  public CategoryType getType() {
    return type;
  }

  public void setType(CategoryType type) {
    this.type = type;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }
}
