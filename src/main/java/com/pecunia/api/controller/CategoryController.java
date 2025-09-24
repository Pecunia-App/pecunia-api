package com.pecunia.api.controller;

import com.pecunia.api.dto.category.CategoryCreateDto;
import com.pecunia.api.dto.category.CategoryDto;
import com.pecunia.api.dto.category.CategoryUpdateDto;
import com.pecunia.api.security.CanAccessCategory;
import com.pecunia.api.security.HasRole;
import com.pecunia.api.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Category Controller. */
@RestController
@Tag(name = "Category Controller", description = "Handle CRUD Category.")
@RequestMapping("/categories")
public class CategoryController {
  @Autowired CategoryService categoryService;

  /**
   * Methode renvoyant tous les categories disponibles, utiles aux admins.
   *
   * @param pageable pagination
   * @return pagination de categories
   */
  @GetMapping
  @Operation(summary = "Return all categories", description = "Role admin require")
  @HasRole("ADMIN")
  public ResponseEntity<Page<CategoryDto>> getAllCategories(Pageable pageable) {
    Page<CategoryDto> categories = categoryService.getAll(pageable);
    return categories.isEmpty()
        ? ResponseEntity.noContent().build()
        : ResponseEntity.ok(categories);
  }

  /**
   * Return a category by id.
   *
   * @param id categoryId
   * @return notFound or ok
   */
  @GetMapping("/{id}")
  @CanAccessCategory
  public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long id) {
    CategoryDto category = categoryService.getCategoryById(id);
    return category == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(category);
  }

  /**
   * Return all categories from an user.
   *
   * @param userId user id
   * @param pageable pagination
   * @return noContent or OK
   */
  @GetMapping("/users/{userId}")
  @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
  public ResponseEntity<Page<CategoryDto>> getAllCategoriesByUser(
      @PathVariable Long userId, Pageable pageable) {
    Page<CategoryDto> categories = categoryService.getUserCategories(userId, pageable);
    return categories.isEmpty()
        ? ResponseEntity.noContent().build()
        : ResponseEntity.ok(categories);
  }

  /**
   * Create a Category.
   *
   * @param category categoryCreateDto
   * @return new category or bad request
   */
  @PostMapping
  @PreAuthorize("hasRole('ADMIN') or #category.userId == authentication.principal.id")
  public ResponseEntity<CategoryCreateDto> createCategory(
      @Valid @RequestBody CategoryCreateDto category) {
    CategoryCreateDto savedCategory = categoryService.create(category);
    return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
  }

  /**
   * Update a category.
   *
   * @param categoryId category id
   * @param dto body à update
   * @return 200 ou 404
   */
  @PutMapping("/{id}")
  @Operation(
      summary = "Update a specific Category by Id",
      description = "Role Admin require or login with correct user id.")
  @CanAccessCategory
  public ResponseEntity<CategoryUpdateDto> updateCategory(
      @PathVariable Long id, @Valid @RequestBody CategoryUpdateDto dto) {
    CategoryUpdateDto updatedCategory = categoryService.update(id, dto);
    return updatedCategory == null
        ? ResponseEntity.notFound().build()
        : ResponseEntity.ok(updatedCategory);
  }

  @DeleteMapping("/{id}")
  @Operation(
      summary = "Delete a specific Category by Id",
      description = "Role admin require or login with correct user id.")
  @CanAccessCategory
  public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
    return categoryService.delete(id)
        ? ResponseEntity.noContent().build()
        : ResponseEntity.notFound().build();
  }
}
