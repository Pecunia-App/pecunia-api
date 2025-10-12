package com.pecunia.api.service;

import com.pecunia.api.dto.category.CategoryCreateDto;
import com.pecunia.api.dto.category.CategoryDto;
import com.pecunia.api.dto.category.CategoryUpdateDto;
import com.pecunia.api.exception.ResourceNotFoundException;
import com.pecunia.api.mapper.CategoryMapper;
import com.pecunia.api.model.Category;
import com.pecunia.api.model.CategoryType;
import com.pecunia.api.model.User;
import com.pecunia.api.repository.CategoryRepository;
import com.pecunia.api.repository.UserRepository;
import java.util.List;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
  @Autowired CategoryRepository categoryRepository;
  @Autowired UserRepository userRepository;
  @Autowired CategoryMapper categoryMapper;
  @Autowired TransactionService transactionService;

  private static void validateCategoryCreation(CategoryCreateDto dto) {
    if (dto.getCategoryName() == null) {
      throw new IllegalArgumentException("Category name cannot be null.");
    }
    if (dto.getType() == null) {
      throw new IllegalArgumentException("CategoryType cannot be null.");
    }
    if (dto.getIsGlobal() == null) {
      throw new IllegalArgumentException("isGLobal cannot be null.");
    }
    if (dto.getColor() == null) {
      throw new IllegalArgumentException("Category color cannot be null.");
    }
    if (dto.getUserId() == null) {
      throw new IllegalArgumentException("User id cannot be null");
    }
  }

  public Page<CategoryDto> getAll(@ParameterObject Pageable pageable) {
    Page<Category> categories = categoryRepository.findAll(pageable);
    return categories.map(categoryMapper::convertToDto);
  }

  public List<CategoryDto> getUserCategories(Long userId) {
    List<Category> categories = categoryRepository.findByUserId(userId);
    return categories.stream().map(categoryMapper::convertToDto).toList();
  }

  public CategoryDto getCategoryById(Long categoryId) {
    Category category = getCategoryByIdOrThrow(categoryId);
    return category != null ? categoryMapper.convertToDto(category) : null;
  }

  public CategoryCreateDto create(CategoryCreateDto dto) {
    validateCategoryCreation(dto);
    User user =
        userRepository
            .findById(dto.getUserId())
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
    Category category = categoryMapper.convertCreateDtoToEntity(dto, user);
    Category savedCategory = categoryRepository.save(category);
    return categoryMapper.convertToCreateDto(savedCategory);
  }

  public CategoryUpdateDto update(Long categoryId, CategoryUpdateDto dto) {
    Category category = getCategoryByIdOrThrow(categoryId);
    CategoryType oldType = category.getType();

    if (dto.getCategoryName() != null) {
      category.setCategoryName(dto.getCategoryName().trim());
    }
    if (dto.getIcon() != null) {
      category.setIcon(dto.getIcon());
    }
    if (dto.getIsGlobal() != null) {
      category.setIsGlobal(dto.getIsGlobal());
    }
    if (dto.getColor() != null) {
      category.setColor(dto.getColor());
    }

    boolean typeChanged = false;
    if (dto.getType() != null && !dto.getType().equals(oldType)) {
      category.setType(dto.getType());
      typeChanged = true;
    }

    Category updatedCategory = categoryRepository.save(category);
    if (typeChanged) {
      transactionService.recalculateWalletBalancesForCategoryTypeChange(
          categoryId, oldType, dto.getType());
    }
    return categoryMapper.convertToUpdateDto(updatedCategory);
  }

  public boolean delete(Long categoryId) {
    Category category = getCategoryByIdOrThrow(categoryId);
    if (category == null) {
      return false;
    }
    categoryRepository.delete(category);
    return true;
  }

  private Category getCategoryByIdOrThrow(Long categoryId) {
    return categoryRepository
        .findById(categoryId)
        .orElseThrow(
            () ->
                new ResourceNotFoundException(
                    "la categorie avec l'id" + categoryId + "n'a pas été trouvée."));
  }

  public List<CategoryDto> getAllGlobalCategories() {
    List<Category> categories = categoryRepository.findAllByIsGlobalTrue();
    return categories.stream().map(categoryMapper::convertToDto).toList();
  }
}
