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

  /**
   * method to search if a wallet are with the user who creates it.
   *
   * @param id wallet id
   * @param userId id du wallet
   * @return boolean
   */
  boolean existsByIdAndUserId(Long id, Long userId);

  /**
   * Récupère le wallet d'un utilisateur par son userId.
   *
   * @param userId id de l'utilisateur
   * @return liste des wallets
   */
  Optional<Wallet> findByUserId(Long userId);
}
