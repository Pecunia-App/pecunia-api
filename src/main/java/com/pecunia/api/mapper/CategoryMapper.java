package com.pecunia.api.mapper;

import com.pecunia.api.dto.category.CategoryCreateDto;
import com.pecunia.api.dto.category.CategoryDto;
import com.pecunia.api.dto.category.CategoryUpdateDto;
import com.pecunia.api.model.Category;
import com.pecunia.api.model.User;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
  public CategoryDto convertToDto(Category category) {
    CategoryDto dto = new CategoryDto();
    dto.setId(category.getId());
    dto.setCategoryName(category.getCategoryName());
    dto.setType(category.getType());
    dto.setColor(category.getColor());
    dto.setIcon(category.getIcon());
    dto.setGlobal(category.getIsGlobal());
    dto.setUserId(category.getUser().getId());
    return dto;
  }

  public Category convertCreateDtoToEntity(CategoryCreateDto dto, User user) {
    Category entity = new Category();
    entity.setCategoryName(dto.getCategoryName());
    entity.setColor(dto.getColor());
    entity.setType(dto.getType());
    entity.setIcon(dto.getIcon());
    entity.setIsGlobal(dto.getIsGlobal());
    entity.setUser(user);
    return entity;
  }

  public CategoryCreateDto convertToCreateDto(Category entity) {
    CategoryCreateDto dto = new CategoryCreateDto();
    dto.setCategoryName(entity.getCategoryName());
    dto.setColor(entity.getColor());
    dto.setIcon(entity.getIcon());
    dto.setIsGlobal(entity.getIsGlobal());
    dto.setType(entity.getType());
    dto.setUserId(entity.getUser().getId());
    return dto;
  }

  public CategoryUpdateDto convertToUpdateDto(Category entity) {
    CategoryUpdateDto dto = new CategoryUpdateDto();
    dto.setCategoryName(entity.getCategoryName());
    dto.setColor(entity.getColor());
    dto.setIcon(entity.getIcon());
    dto.setIsGlobal(entity.getIsGlobal());
    dto.setType(entity.getType());
    return dto;
  }
}
