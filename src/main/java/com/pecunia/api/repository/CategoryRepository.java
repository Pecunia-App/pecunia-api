package com.pecunia.api.repository;

import com.pecunia.api.model.Category;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/** CategoryRepository. */
public interface CategoryRepository extends JpaRepository<Category, Long> {
  List<Category> findByUserId(Long userId);

  List<Category> findAllByIsGlobalTrue();

  boolean existsByIdAndUserId(Long id, Long userId);
}
