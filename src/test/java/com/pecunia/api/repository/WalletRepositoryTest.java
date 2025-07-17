package com.pecunia.api.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.pecunia.api.model.Wallet;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class WalletRepositoryTest {

  @Autowired private WalletRepository walletRepository;

  @Test
  void testFindAllTransactions() {
    final Wallet wallet1 = new Wallet();
    wallet1.setName("Wallet de John");
    wallet1.setAmount(new BigDecimal(2000.34));
    wallet1.setCurrency("EUR");
    wallet1.setAmountBalance(wallet1.getAmountBalance());

    final Wallet wallet2 = new Wallet();
    wallet1.setName("Wallet de John");
    wallet1.setAmount(new BigDecimal("-1000"));
    wallet1.setCurrency("EUR");
    wallet1.setAmountBalance(wallet1.getAmountBalance());

    walletRepository.saveAll(List.of(wallet1, wallet2));

    List<Wallet> wallets = walletRepository.findAll();

    assertThat(wallets).hasSize(2);

    assertThat(wallets.get(0)).isEqualTo(wallet1);
    assertThat(wallets.get(1)).isEqualTo(wallet2);
  }

  @Test
  void testFindByName() {
    Wallet wallet = new Wallet();
    wallet.setName("Wallet de Jane");

    walletRepository.save(wallet);

    Optional<Wallet> result = walletRepository.findByName(wallet.getName());

    assertThat(result.get().getName()).hasToString("Wallet de Jane");
    assertThat(result.get().getName()).isNotEqualTo("Bad Wallet");
  }
}
