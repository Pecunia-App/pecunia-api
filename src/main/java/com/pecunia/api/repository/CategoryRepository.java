package com.pecunia.api.repository;

import com.pecunia.api.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/** CategoryRepository. */
public interface CategoryRepository extends JpaRepository<Category, Long> {
  Page<Category> findByUserId(Long userId, Pageable pageable);

  boolean existsByIdAndUserId(Long id, Long userId);
}
