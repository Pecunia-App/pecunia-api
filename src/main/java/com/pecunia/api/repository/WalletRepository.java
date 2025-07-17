package com.pecunia.api.repository;

import com.pecunia.api.model.Wallet;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/** WalletRepository. */
public interface WalletRepository extends JpaRepository<Wallet, Long> {
  /**
   * Find Wallet by name.
   *
   * @param name of wallet.
   * @return wallet.
   */
  Optional<Wallet> findByName(String name);
}
