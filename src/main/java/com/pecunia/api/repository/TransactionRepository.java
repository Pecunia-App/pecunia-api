package com.pecunia.api.repository;

import com.pecunia.api.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/** TransactionRepository. */
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
  /**
   * Find all transactions by Wallet.
   *
   * @param walletId wallet id
   * @param pageable pageSize, pageNumber, sort=createdAt,asc or desc
   * @return transactions
   */
  Page<Transaction> findByWalletId(Long walletId, Pageable pageable);
}
