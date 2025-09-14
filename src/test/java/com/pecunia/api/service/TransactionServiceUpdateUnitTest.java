package com.pecunia.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pecunia.api.dto.transaction.TransactionUpdateDto;
import com.pecunia.api.exception.ResourceNotFoundException;
import com.pecunia.api.mapper.TransactionMapper;
import com.pecunia.api.model.Category;
import com.pecunia.api.model.CategoryType;
import com.pecunia.api.model.Money;
import com.pecunia.api.model.Tag;
import com.pecunia.api.model.Transaction;
import com.pecunia.api.model.Wallet;
import com.pecunia.api.repository.CategoryRepository;
import com.pecunia.api.repository.TagRepository;
import com.pecunia.api.repository.TransactionRepository;
import com.pecunia.api.repository.WalletRepository;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class TransactionServiceUpdateUnitTest {
  @Mock private TransactionRepository transactionRepository;
  @Mock private WalletRepository walletRepository;
  @Mock private TagRepository tagRepository;
  @Mock private TransactionMapper transactionMapper;
  @Mock private CategoryRepository categoryRepository;

  @InjectMocks private TransactionService transactionService;

  private Transaction mockTransaction;
  private Wallet mockWallet;
  private Category mockCategory;

  @BeforeEach
  void setup() {
    new Money();
    mockWallet = new Wallet();
    mockWallet.setId(1L);
    mockWallet.setAmountBalance(Money.euros(800.00));

    mockCategory = new Category();
    mockCategory.setId(1L);
    mockCategory.setCategoryName("category name");
    mockCategory.setType(CategoryType.DEBIT);

    mockTransaction = new Transaction();
    mockTransaction.setId(1L);
    mockTransaction.setAmount(Money.euros(200));
    mockTransaction.setWallet(mockWallet);
    mockTransaction.setCategory(mockCategory);
  }

  @Test
  void shouldThrowIllegalArgumentExceptionWhenTransactionNotFound() {
    Long nonExistendId = 99L;
    when(transactionRepository.findById(nonExistendId)).thenReturn(Optional.empty());

    assertThrows(
        ResourceNotFoundException.class,
        () -> transactionService.update(nonExistendId, new TransactionUpdateDto()));
  }

  @Test
  void shouldThrowIllegalArgumentExceptionWhenTagsNotFound() {
    when(transactionRepository.findById(1L)).thenReturn(Optional.of(mockTransaction));
    when(tagRepository.findAllById(Set.of(1L, 2L))).thenReturn(List.of(new Tag()));

    TransactionUpdateDto updateDto = new TransactionUpdateDto();
    updateDto.setTagsIds(Set.of(1L, 2L));

    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class, () -> transactionService.update(1L, updateDto));
    assertEquals("Certains tags n'existent pas.", exception.getMessage());
  }

  @Test
  void shouldUpdateNoteField() {
    when(transactionRepository.findById(1L)).thenReturn(Optional.of(mockTransaction));
    when(categoryRepository.findById(1L)).thenReturn(Optional.of(mockCategory));
    when(walletRepository.findById(any())).thenReturn(Optional.of(mockWallet));
    when(transactionRepository.save(any())).thenReturn(mockTransaction);

    TransactionUpdateDto updateDto = new TransactionUpdateDto();
    updateDto.setCategoryId(mockCategory.getId());
    updateDto.setNote("Nouvelle note");

    transactionService.update(1L, updateDto);

    assertEquals("Nouvelle note", mockTransaction.getNote());
    assertEquals(mockCategory.getId(), mockTransaction.getCategory().getId());
    verify(transactionRepository).save(mockTransaction);
  }

  @Test
  void shouldUpdateCreatedAtField() {
    final LocalDateTime newDate = LocalDateTime.now().minusDays(5);
    TransactionUpdateDto updateDto = new TransactionUpdateDto();
    updateDto.setCreatedAt(newDate);
  }

  @Test
  void shouldHandleNullFieldsWithoutChangingOriginalValues() {
    final Money originalAmount = mockTransaction.getAmount();
    final CategoryType originalType = mockTransaction.getCategoryType();

    when(transactionRepository.findById(1L)).thenReturn(Optional.of(mockTransaction));
    when(walletRepository.findById(any())).thenReturn(Optional.of(mockWallet));
    when(transactionRepository.save(any())).thenReturn(mockTransaction);

    TransactionUpdateDto updateDto = new TransactionUpdateDto();

    transactionService.update(1L, updateDto);

    assertEquals(originalAmount, mockTransaction.getAmount());
    assertEquals(originalType, mockTransaction.getCategoryType());
  }

  @Test
  void shouldCallAllRequiredRepositories() {
    when(transactionRepository.findById(1L)).thenReturn(Optional.of(mockTransaction));
    when(walletRepository.findById(any())).thenReturn(Optional.of(mockWallet));
    when(transactionRepository.save(any())).thenReturn(mockTransaction);
    when(transactionMapper.convertToUpdateDto(any())).thenReturn(new TransactionUpdateDto());

    TransactionUpdateDto updateDto = new TransactionUpdateDto();
    updateDto.setNote("Test");

    transactionService.update(1L, updateDto);

    verify(transactionRepository).findById(1L);
    verify(walletRepository).findById(mockWallet.getId());
    verify(transactionRepository).save(mockTransaction);
    verify(transactionMapper).convertToUpdateDto(mockTransaction);
  }

  @Test
  void shouldValidateTagsWhenTagsIdsProvided() {
    Set<Long> tagIds = Set.of(1L, 2L);
    List<Tag> foundTags = List.of(new Tag(), new Tag());

    when(transactionRepository.findById(1L)).thenReturn(Optional.of(mockTransaction));
    when(tagRepository.findAllById(tagIds)).thenReturn(foundTags);
    when(walletRepository.findById(any())).thenReturn(Optional.of(mockWallet));
    when(transactionRepository.save(any())).thenReturn(mockTransaction);

    TransactionUpdateDto updateDto = new TransactionUpdateDto();
    updateDto.setTagsIds(tagIds);

    transactionService.update(1L, updateDto);

    verify(tagRepository).findAllById(tagIds);
    assertEquals(new HashSet<>(foundTags), mockTransaction.getTags());
  }
}
