package com.pecunia.api.repository;

import com.pecunia.api.model.Provider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/** ProviderRepository. */
public interface ProviderRepository extends JpaRepository<Provider, Long> {
  /**
   * Find all providers by User.
   *
   * @param providerId provider id
   * @param pageable pageSize, pageNumber, sort=createdAt,asc or desc
   * @return providers
   */
  Page<Provider> findByUserId(Long providerId, Pageable pageable);
}
