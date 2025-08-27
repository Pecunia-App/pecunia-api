package com.pecunia.api.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.pecunia.api.dto.transaction.TransactionCreateDto;
import com.pecunia.api.mapper.TransactionMapper;
import com.pecunia.api.model.Money;
import com.pecunia.api.model.Tag;
import com.pecunia.api.model.Transaction;
import com.pecunia.api.model.TransactionType;
import com.pecunia.api.model.Wallet;
import com.pecunia.api.repository.TagRepository;
import com.pecunia.api.repository.TransactionRepository;
import com.pecunia.api.repository.WalletRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class TransactionServiceCreateUnitTest {

  @Mock private WalletRepository walletRepository;
  @Mock private TagRepository tagRepository;
  @Mock private TransactionRepository transactionRepository;
  @Mock private TransactionMapper transactionMapper;
  @InjectMocks private TransactionService transactionService;

  @Test
  void shouldCreateATransactionAndUpdateWalletBalance() {
    Long walletId = 1L;
    Long tagId = 1L;
    Set<Long> tagIds = Set.of(tagId);

    TransactionCreateDto createDto = new TransactionCreateDto();
    createDto.setWalletId(walletId);
    createDto.setAmount(new Money().euros(123.54));
    createDto.setType(TransactionType.DEBIT);
    createDto.setNote("Test transaction");
    createDto.setTagsIds(tagIds);

    Wallet mockWallet = new Wallet();
    mockWallet.setId(walletId);
    mockWallet.setAmountBalance(new Money().euros(500.00));

    Tag mockTag = new Tag();
    mockTag.setId(tagId);
    final Set<Tag> mockTags = Set.of(mockTag);

    Transaction mockTransaction = new Transaction();
    mockTransaction.setId(1L);
    mockTransaction.setAmount(createDto.getAmount());
    mockTransaction.setType(createDto.getType());

    Transaction savedTransaction = new Transaction();
    savedTransaction.setId(1L);

    TransactionCreateDto expectedResult = new TransactionCreateDto();
    expectedResult.setAmount(createDto.getAmount());
    expectedResult.setType(createDto.getType());

    // Assert
    when(walletRepository.findById(walletId)).thenReturn(Optional.of(mockWallet));
    when(tagRepository.findAllById(tagIds)).thenReturn(List.of(mockTag));
    when(transactionMapper.convertCreateDtoToEntity(createDto, mockWallet, mockTags))
        .thenReturn(mockTransaction);
    when(transactionRepository.save(mockTransaction)).thenReturn(savedTransaction);
    when(transactionMapper.convertToCreateDto(savedTransaction)).thenReturn(expectedResult);

    TransactionCreateDto result = transactionService.create(createDto);

    assertThat(result).isNotNull();
    assertEquals(new Money().euros(123.54), result.getAmount());
    verify(walletRepository).findById(walletId);
    verify(tagRepository).findAllById(tagIds);
    verify(transactionRepository).save(mockTransaction);
    verify(transactionMapper).convertCreateDtoToEntity(createDto, mockWallet, mockTags);
    verify(transactionMapper).convertToCreateDto(savedTransaction);
  }

  @Test
  void shouldThrowExceptionWhenWalletNotFound() {
    Long walletId = 999L;
    TransactionCreateDto createDto = new TransactionCreateDto();
    createDto.setWalletId(walletId);
    createDto.setAmount(new Money().euros(100.00));
    createDto.setType(TransactionType.DEBIT);
    createDto.setNote("Test transaction");

    when(walletRepository.findById(walletId)).thenReturn(Optional.empty());

    assertThrows(
        IllegalArgumentException.class,
        () -> transactionService.create(createDto),
        "Wallet not found");

    verify(walletRepository).findById(walletId);
    verifyNoInteractions(tagRepository, transactionRepository, transactionMapper);
  }

  @Test
  void shouldCreateTransactionWithoutTags() {
    Long walletId = 1L;

    TransactionCreateDto createDto = new TransactionCreateDto();
    createDto.setWalletId(walletId);
    createDto.setAmount(new Money().euros(50.00));
    createDto.setType(TransactionType.DEBIT);
    createDto.setNote("Transaction sans tags");
    createDto.setTagsIds(null);

    Wallet mockWallet = new Wallet();
    mockWallet.setId(walletId);
    mockWallet.setAmountBalance(new Money().euros(0));

    final Transaction mockTransaction = new Transaction();
    Transaction savedTransaction = new Transaction();
    savedTransaction.setId(1L);

    TransactionCreateDto expectedResult = new TransactionCreateDto();
    expectedResult.setType(TransactionType.DEBIT);

    when(walletRepository.findById(walletId)).thenReturn(Optional.of(mockWallet));
    when(transactionMapper.convertCreateDtoToEntity(createDto, mockWallet, new HashSet<>()))
        .thenReturn(mockTransaction);
    when(transactionRepository.save(mockTransaction)).thenReturn(savedTransaction);
    when(transactionMapper.convertToCreateDto(savedTransaction)).thenReturn(expectedResult);

    TransactionCreateDto result = transactionService.create(createDto);

    assertNotNull(result);
    verify(walletRepository).findById(walletId);
    verify(transactionRepository).save(mockTransaction);
    verifyNoInteractions(tagRepository);
  }

  @Test
  void shouldCreateTransactionWithEmptyTagSet() {
    Long walletId = 1L;

    TransactionCreateDto createDto = new TransactionCreateDto();
    createDto.setWalletId(walletId);
    createDto.setAmount(new Money().euros(75.00));
    createDto.setType(TransactionType.DEBIT);
    createDto.setTagsIds(new HashSet<>());

    Wallet mockWallet = new Wallet();
    mockWallet.setAmountBalance(new Money().euros(10));
    final Transaction mockTransaction = new Transaction();
    final Transaction savedTransaction = new Transaction();
    TransactionCreateDto expectedResult = new TransactionCreateDto();
    expectedResult.setAmount(new Money().euros(10));

    when(walletRepository.findById(walletId)).thenReturn(Optional.of(mockWallet));
    when(tagRepository.findAllById(new HashSet<>())).thenReturn(List.of());
    when(transactionMapper.convertCreateDtoToEntity(createDto, mockWallet, new HashSet<>()))
        .thenReturn(mockTransaction);
    when(transactionRepository.save(mockTransaction)).thenReturn(savedTransaction);
    when(transactionMapper.convertToCreateDto(savedTransaction)).thenReturn(expectedResult);

    TransactionCreateDto result = transactionService.create(createDto);

    assertNotNull(result);
    verify(tagRepository).findAllById(new HashSet<>());
  }

  private Tag createTag(Long id) {
    Tag tag = new Tag();
    tag.setId(id);
    return tag;
  }
}
