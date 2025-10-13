package com.pecunia.api.dto.category;

import com.pecunia.api.model.CategoryType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CategoryDto {
  private Long id;

  @NotEmpty(message = "Category name cannt be empty.")
  @Size(min = 1, max = 10, message = "le nom de la catégorie ne doit pas dépassé 10 caractères.")
  private String categoryName;

  private String icon;

  @Pattern(
      regexp =
          "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$|^background-badge-(blue|green|grey|orange|pink|red|violet|yellow)-(low|medium)$|^background-badge-grey$",
      message =
          "La couleur doit être au format hexadécimal (ex: #FF5733) ou un token valide (ex:"
              + " background-badge-blue-low)")
  private String color;

  private CategoryType type;

  @NotNull(message = "is_Global cannot be null")
  private boolean isGlobal;

  private Long userId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

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

  public CategoryType getType() {
    return type;
  }

  public void setType(CategoryType type) {
    this.type = type;
  }

  public boolean isGlobal() {
    return isGlobal;
  }

  public void setGlobal(boolean isGlobal) {
    this.isGlobal = isGlobal;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }
}
