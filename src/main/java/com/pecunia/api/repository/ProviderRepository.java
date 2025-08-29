package com.pecunia.api.repository;

import com.pecunia.api.model.Provider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

/** ProviderRepository. */
public interface ProviderRepository extends JpaRepository<Provider, Long> {
  /**
   * Find all providers by User.
   *
   * @param userId user id
   * @param pageable pageSize, pageNumber, sort=createdAt,asc or desc
   * @return providers
   */
  Page<Provider> findByUserId(Long userId, Pageable pageable);

  Page<Provider> findByProviderNameContainingIgnoreCaseAndTransactionsWalletUserId(
      @Param("providerName") String providerName, Long userId, Pageable pageable);

  boolean existsByIdAndUserId(Long id, Long userId);
}
