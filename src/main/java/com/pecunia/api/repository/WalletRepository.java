package com.pecunia.api.repository;

import com.pecunia.api.model.Wallet;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
  Optional<Wallet> findByName(String name);
}
