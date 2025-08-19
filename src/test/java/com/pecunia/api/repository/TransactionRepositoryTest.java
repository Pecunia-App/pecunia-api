package com.pecunia.api.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.pecunia.api.builder.TransactionFactory;
import com.pecunia.api.builder.UserFactory;
import com.pecunia.api.builder.WalletFactory;
import com.pecunia.api.model.Money;
import com.pecunia.api.model.Transaction;
import com.pecunia.api.model.TransactionType;
import com.pecunia.api.model.User;
import com.pecunia.api.model.Wallet;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

@DataJpaTest
class TransactionRepositoryTest {

  @Autowired private TransactionRepository transactionRepository;
  @Autowired private TestEntityManager entityManager;

  @Test
  void testFindAllTransactions() {
    User user1 =
        UserFactory.user().withName("John", "Doe").withEmail("user1@test.fr").build(entityManager);

    Money amountTransaction1 =
        new Money(new BigDecimal("1203.45"), Currency.getInstance("EUR"), RoundingMode.HALF_EVEN);
    Wallet wallet1 =
        WalletFactory.wallet()
            .withName("Wallet1")
            .withAmount(amountTransaction1)
            .forUser(user1)
            .build(entityManager);

    Transaction transaction1 =
        TransactionFactory.transaction()
            .withType(TransactionType.DEBIT)
            .withNote("TEst note")
            .withAmount(amountTransaction1)
            .forWallet(wallet1)
            .build(entityManager);

    User user2 =
        UserFactory.user().withName("Jane", "Doe").withEmail("user2@test.fr").build(entityManager);

    Money amountTransaction2 =
        new Money(new BigDecimal("120.45"), Currency.getInstance("JPY"), RoundingMode.HALF_EVEN);
    Wallet wallet2 =
        WalletFactory.wallet()
            .withName("Wallet2")
            .withAmount(amountTransaction2)
            .forUser(user2)
            .build(entityManager);

    Transaction transaction2 =
        TransactionFactory.transaction()
            .withType(TransactionType.CREDIT)
            .withNote("debit note")
            .withAmount(amountTransaction2)
            .forWallet(wallet2)
            .build(entityManager);

    transactionRepository.saveAll(List.of(transaction1, transaction2));
    Pageable pageable = PageRequest.of(0, 5, Direction.ASC, "type");
    Page<Transaction> transactions = transactionRepository.findAll(pageable);
    assertThat(transactions).hasSize(2);
    assertThat(transactions.getContent().get(0).getType())
        .isEqualByComparingTo(TransactionType.DEBIT);
    assertThat(transactions.getContent().get(1).getType())
        .isEqualByComparingTo(TransactionType.CREDIT);
    assertThat(transactions.getContent().get(0).getAmount().getCurrencyCode()).isIn("EUR", "JPY");
    assertThat(transactions.getContent().get(1).getAmount().getCurrencyCode()).isIn("EUR", "JPY");
    assertThat(transactions.getPageable().getPageSize()).isEqualTo(5);
  }
}
