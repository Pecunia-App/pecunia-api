package com.pecunia.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.pecunia.api.dto.transaction.TransactionUpdateDto;
import com.pecunia.api.model.Category;
import com.pecunia.api.model.CategoryType;
import com.pecunia.api.model.Money;
import com.pecunia.api.model.Provider;
import com.pecunia.api.model.Tag;
import com.pecunia.api.model.Transaction;
import com.pecunia.api.model.User;
import com.pecunia.api.model.Wallet;
import com.pecunia.api.repository.CategoryRepository;
import com.pecunia.api.repository.ProviderRepository;
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
  @Autowired private ProviderRepository providerRepository;
  @Autowired private CategoryRepository categoryRepository;
  @Autowired private CategoryService categoryService;

  private Wallet testWallet;
  private Transaction testTransaction;
  private Tag testTag;
  private User testUser;
  private Provider testProvider;
  private Category testCategory;

  @BeforeEach
  void setUp() {
    new Money();
    testUser = new User();
    testUser.setEmail("test@test.fr");
    testUser.setLastname("lastname");
    testUser.setFirstname("firstname");
    testUser = userRepository.save(testUser);

    testWallet = new Wallet();
    testWallet.setAmountBalance(Money.euros(1000.00));
    testWallet.setName("test wallet");
    testWallet.setUser(testUser);
    testWallet = walletRepository.save(testWallet);

    testUser.setWallet(testWallet);

    testUser.setWallet(testWallet);
    testUser = userRepository.save(testUser);

    testTag = new Tag();
    testTag.setTagName("TestTag");
    testTag = tagRepository.save(testTag);

    testProvider = new Provider();
    testProvider.setProviderName("testPRovider");
    testProvider.setUser(testUser);
    testProvider = providerRepository.save(testProvider);

    testCategory = new Category();
    testCategory.setCategoryName("testCategoryname");
    testCategory.setUser(testUser);
    testCategory.setType(CategoryType.DEBIT);
    testCategory.setIsGlobal(false);
    testCategory.setIcon("icon");
    testCategory.setColor("white");
    testCategory = categoryRepository.save(testCategory);

    testTransaction = new Transaction();
    testTransaction.setAmount(Money.euros(200.00));
    testTransaction.setWallet(testWallet);
    testTransaction.setProvider(testProvider);
    testTransaction.setCategory(testCategory);
    testTransaction = transactionRepository.save(testTransaction);

    testWallet.setAmountBalance(Money.euros(800.00));
    testWallet = walletRepository.save(testWallet);
  }

  @Test
  void shouldUpdateAmountAndRecalculateBalance() {
    TransactionUpdateDto updateDto = new TransactionUpdateDto();
    updateDto.setAmount(Money.euros(300.00));

    transactionService.update(testTransaction.getId(), updateDto);
    Wallet updatedWallet = walletRepository.findById(testWallet.getId()).get();
    Money expectedBalance = Money.euros(700.00);
    // Calcul = 800 + 200 - 300 = 700
    assertEquals(expectedBalance, updatedWallet.getAmountBalance());
  }

  @Test
  void shouldUpdateCategoryTypeAndRecalculateBalance() {
    Category creditCategory = new Category();
    creditCategory.setCategoryName("Credit Category");
    creditCategory.setUser(testUser);
    creditCategory.setType(CategoryType.CREDIT);
    creditCategory.setIsGlobal(false);
    creditCategory.setIcon("icon");
    creditCategory.setColor("blue");
    creditCategory = categoryRepository.save(creditCategory);

    TransactionUpdateDto updateDto = new TransactionUpdateDto();
    updateDto.setCategoryId(creditCategory.getId());
    transactionService.update(testTransaction.getId(), updateDto);

    Wallet updatedWallet = walletRepository.findById(testWallet.getId()).get();
    Transaction updatedTransaction = transactionRepository.findById(testTransaction.getId()).get();
    assertEquals(CategoryType.CREDIT, updatedTransaction.getCategoryType());
    // Calcul = 800 + 200 + 200 = 1200
    assertEquals(Money.euros(1200), updatedWallet.getAmountBalance());
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
