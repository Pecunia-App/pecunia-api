package com.pecunia.api.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.pecunia.api.builder.UserFactory;
import com.pecunia.api.builder.WalletFactory;
import com.pecunia.api.model.Money;
import com.pecunia.api.model.User;
import com.pecunia.api.model.Wallet;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
class WalletRepositoryTest {

  @Autowired private WalletRepository walletRepository;
  @Autowired private TestEntityManager entityManager;

  @Test
  void testFindAllTransactions() {
    User user1 =
        UserFactory.user().withName("John", "Doe").withEmail("user1@test.fr").build(entityManager);
    Money amountBalanceWallet1 =
        new Money(new BigDecimal("1230.45"), Currency.getInstance("EUR"), RoundingMode.HALF_EVEN);
    Wallet wallet1 =
        WalletFactory.wallet()
            .withName("Wallet de John")
            .withAmount(amountBalanceWallet1)
            .forUser(user1)
            .build(entityManager);

    User user2 =
        UserFactory.user().withName("Jane", "Doe").withEmail("user2@test.fr").build(entityManager);
    Money amountBalanceWallet2 =
        new Money(new BigDecimal("-1000"), Currency.getInstance("USD"), RoundingMode.HALF_EVEN);
    Wallet wallet2 =
        WalletFactory.wallet()
            .withName("Wallet de Jane")
            .withAmount(amountBalanceWallet2)
            .forUser(user2)
            .build(entityManager);

    walletRepository.saveAll(List.of(wallet1, wallet2));

    List<Wallet> wallets = walletRepository.findAll();
    assertThat(wallets).hasSize(2);
    assertThat(wallets)
        .extracting(Wallet::getName)
        .containsExactlyInAnyOrder("Wallet de John", "Wallet de Jane");

    assertThat(wallets)
        .extracting(Wallet::getAmountBalance)
        .containsExactlyInAnyOrder(amountBalanceWallet1, amountBalanceWallet2);
  }

  @Test
  void testFindByName() {
    User user =
        UserFactory.user().withName("John", "Doe").withEmail("user1@test.fr").build(entityManager);
    Wallet wallet =
        WalletFactory.wallet()
            .withName("Wallet de Jane")
            .withAmount(Money.euros(500.73))
            .forUser(user)
            .build(entityManager);

    walletRepository.save(wallet);

    Optional<Wallet> result = walletRepository.findByName(wallet.getName());

    assertThat(result.get().getName()).hasToString("Wallet de Jane");
    assertThat(result.get().getName()).isNotEqualTo("Bad Wallet");
    assertThat(result.get().getAmountBalance().amount())
        .isEqualByComparingTo(new BigDecimal("500.73"));
    assertThat(result.get().getAmountBalance().getCurrencyCode()).isEqualTo("EUR");
  }

  @Test
  void testMoneyOperations() {
    User user1 =
        UserFactory.user().withName("John", "Doe").withEmail("user1@test.fr").build(entityManager);
    Wallet wallet1 =
        WalletFactory.wallet()
            .withName("Wallet de Jane")
            .withAmount(Money.euros(100.50))
            .forUser(user1)
            .build(entityManager);
    User user2 =
        UserFactory.user().withName("Jane", "Doe").withEmail("user2@test.fr").build(entityManager);

    Wallet wallet2 =
        WalletFactory.wallet()
            .withName("Wallet de Jane")
            .withAmount(Money.euros(200.25))
            .forUser(user2)
            .build(entityManager);

    walletRepository.saveAll(List.of(wallet1, wallet2));

    List<Wallet> savedWallets = walletRepository.findAll();

    Money totalAmount =
        savedWallets.get(0).getAmountBalance().add(savedWallets.get(1).getAmountBalance());

    assertThat(totalAmount.amount()).isEqualByComparingTo(new BigDecimal("300.75"));
    assertThat(totalAmount.getCurrencyCode()).isEqualTo("EUR");
  }

  @Test
  void testWalletWithDifferentCurrencies() {
    User user1 =
        UserFactory.user().withName("John", "Doe").withEmail("user1@test.fr").build(entityManager);
    Wallet walletEur =
        WalletFactory.wallet()
            .withName("Euros wallet")
            .withAmount(Money.euros(100.50))
            .forUser(user1)
            .build(entityManager);
    User user2 =
        UserFactory.user().withName("Jane", "Doe").withEmail("user2@test.fr").build(entityManager);

    Wallet walletUsd =
        WalletFactory.wallet()
            .withName("Dollars wallet")
            .withAmount(Money.dollars(200.25))
            .forUser(user2)
            .build(entityManager);

    walletRepository.saveAll(List.of(walletEur, walletUsd));

    List<Wallet> wallets = walletRepository.findAll();

    assertThat(wallets).hasSize(2);
    assertThat(wallets.get(0).getAmountBalance().getCurrencyCode()).isIn("EUR", "USD");
    assertThat(wallets.get(1).getAmountBalance().getCurrencyCode()).isIn("EUR", "USD");

    assertThat(wallets.get(0).getAmountBalance().getCurrencyCode())
        .isNotEqualTo(wallets.get(1).getAmountBalance().getCurrencyCode());
  }
}
