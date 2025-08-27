package com.pecunia.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.pecunia.api.dto.transaction.TransactionUpdateDto;
import com.pecunia.api.model.Money;
import com.pecunia.api.model.Tag;
import com.pecunia.api.model.Transaction;
import com.pecunia.api.model.TransactionType;
import com.pecunia.api.model.User;
import com.pecunia.api.model.Wallet;
import com.pecunia.api.repository.TagRepository;
import com.pecunia.api.repository.TransactionRepository;
import com.pecunia.api.repository.UserRepository;
import com.pecunia.api.repository.WalletRepository;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class TransactionServiceUpdateIntegrationTest {
  @Autowired private TransactionService transactionService;
  @Autowired private TransactionRepository transactionRepository;
  @Autowired private WalletRepository walletRepository;
  @Autowired private TagRepository tagRepository;
  @Autowired private UserRepository userRepository;

  private Wallet testWallet;
  private Transaction testTransaction;
  private Tag testTag;
  private User testUser;

  @BeforeEach
  void setUp() {
    testUser = new User();
    testUser.setEmail("test@test.fr");
    testUser.setLastname("lastname");
    testUser.setFirstname("firstname");
    testUser = userRepository.save(testUser);

    testWallet = new Wallet();
    testWallet.setAmountBalance(new Money().euros(1000.00));
    testWallet.setName("test wallet");
    testWallet.setUser(testUser);
    testWallet = walletRepository.save(testWallet);

    testUser.setWallet(testWallet);

    testUser.setWallet(testWallet);
    testUser = userRepository.save(testUser);

    testTag = new Tag();
    testTag.setTagName("TestTag");
    testTag = tagRepository.save(testTag);

    testTransaction = new Transaction();
    testTransaction.setAmount(new Money().euros(200.00));
    testTransaction.setType(TransactionType.DEBIT);
    testTransaction.setWallet(testWallet);
    testTransaction = transactionRepository.save(testTransaction);

    testWallet.setAmountBalance(new Money().euros(800.00));
    testWallet = walletRepository.save(testWallet);
  }

  @Test
  void shouldUpdateAmountAndRecalculateBalance() {
    TransactionUpdateDto updateDto = new TransactionUpdateDto();
    updateDto.setAmount(new Money().euros(300.00));

    transactionService.update(testTransaction.getId(), updateDto);
    Wallet updatedWallet = walletRepository.findById(testWallet.getId()).get();
    Money expectedBalance = new Money().euros(700.00);
    // Calcul = 800 + 200 - 300 = 700
    assertEquals(expectedBalance, updatedWallet.getAmountBalance());
  }

  @Test
  void shouldUpdateTypeAndRecalculateBalance() {
    TransactionUpdateDto updateDto = new TransactionUpdateDto();
    updateDto.setType(TransactionType.CREDIT);

    transactionService.update(testTransaction.getId(), updateDto);

    Wallet updatedWallet = walletRepository.findById(testWallet.getId()).get();
    Transaction updatedTransaction = transactionRepository.findById(testTransaction.getId()).get();
    assertEquals(TransactionType.CREDIT, updatedTransaction.getType());
    // Calcul = 800 + 200 + 200 = 1200
    assertEquals(new Money().euros(1200), updatedWallet.getAmountBalance());
  }

  @Test
  void shouldUpdateTagsWithDatabaseConstraints() {
    Tag secondTag = new Tag();
    secondTag.setTagName("second tag");
    tagRepository.save(secondTag);

    TransactionUpdateDto updateDto = new TransactionUpdateDto();
    updateDto.setTagsIds(Set.of(testTag.getId(), secondTag.getId()));

    transactionService.update(testTransaction.getId(), updateDto);

    Transaction updatedTransaction = transactionRepository.findById(testTransaction.getId()).get();
    assertEquals(2, updatedTransaction.getTags().size());
    assertTrue(updatedTransaction.getTags().contains(testTag));
    assertTrue(updatedTransaction.getTags().contains(secondTag));
  }
}
