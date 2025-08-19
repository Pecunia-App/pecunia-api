package com.pecunia.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pecunia.api.dto.transaction.TransactionDto;
import com.pecunia.api.dto.wallet.WalletCreateDto;
import com.pecunia.api.dto.wallet.WalletDto;
import com.pecunia.api.dto.wallet.WalletUpdateDto;
import com.pecunia.api.mapper.TransactionMapper;
import com.pecunia.api.mapper.WalletMapper;
import com.pecunia.api.model.Money;
import com.pecunia.api.model.Transaction;
import com.pecunia.api.model.User;
import com.pecunia.api.model.Wallet;
import com.pecunia.api.repository.TransactionRepository;
import com.pecunia.api.repository.UserRepository;
import com.pecunia.api.repository.WalletRepository;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.Currency;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class WalletServiceTest {
  @Mock private WalletRepository walletRepository;
  @Mock private TransactionRepository transactionRepository;
  @Mock private UserRepository userRepository;

  @Mock private WalletMapper walletMapper;
  @Mock private TransactionMapper transactionMapper;

  @InjectMocks private WalletService walletService;

  @Test
  void testGetAllWallets() {
    Wallet wallet = new Wallet();
    wallet.setId(1L);
    List<Wallet> wallets = Collections.singletonList(wallet);
    WalletDto walletDto = new WalletDto();

    when(walletRepository.findAll()).thenReturn(wallets);
    when(walletMapper.convertToDto(wallet)).thenReturn(walletDto);

    List<WalletDto> walletDtos = walletService.getAllWallets();

    assertEquals(1, walletDtos.size());
    verify(walletRepository).findAll();
    verify(walletMapper).convertToDto(wallet);
  }

  @Test
  void shouldReturnAllTransactionsFromWallet() {
    Wallet wallet = new Wallet();
    Long walletId = 1L;
    final Pageable pageable = PageRequest.of(0, 20);
    wallet.setId(walletId);

    when(walletRepository.existsById(walletId)).thenReturn(true);
    Transaction transaction = new Transaction();
    Page<Transaction> transactions = new PageImpl<>(Collections.singletonList(transaction));
    transaction.setId(1L);
    when(transactionRepository.findByWalletId(walletId, pageable)).thenReturn(transactions);

    TransactionDto transactionDto = new TransactionDto();

    when(transactionMapper.convertToDto(transaction)).thenReturn(transactionDto);

    Page<TransactionDto> result = walletService.getTransactionsWallet(walletId, pageable);
    assertNotNull(result);
    assertEquals(1, result.getContent().size());
    verify(walletRepository).existsById(walletId);
    verify(transactionRepository).findByWalletId(walletId, pageable);
    verify(transactionMapper).convertToDto(transaction);
  }

  @Test
  void shouldThrowExceptionIfWalletNotFound() {
    Long badWalletId = 999L;
    when(walletRepository.existsById(badWalletId)).thenReturn(false);

    assertThrows(
        EntityNotFoundException.class,
        () -> walletService.getTransactionsWallet(badWalletId, PageRequest.of(0, 20)));
    verify(walletRepository).existsById(badWalletId);
  }

  @Test
  void testGetWalletById() {
    Long walletId = 1L;
    Wallet wallet = new Wallet();
    wallet.setId(walletId);
    WalletDto walletDto = new WalletDto();

    when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
    when(walletMapper.convertToDto(wallet)).thenReturn(walletDto);

    WalletDto result = walletService.getWalletById(walletId);

    assertNotNull(result);
    verify(walletRepository).findById(walletId);
    verify(walletMapper).convertToDto(wallet);
  }

  @Test
  void testUpdate() {
    Long walletId = 1L;
    WalletUpdateDto walletUpdateDto = new WalletUpdateDto();
    walletUpdateDto.setName("Updated Wallet");
    walletUpdateDto.setAmount(
        new Money(new BigDecimal("100"), Currency.getInstance("EUR"), RoundingMode.HALF_EVEN));

    Wallet wallet = new Wallet();
    wallet.setId(walletId);
    wallet.setName("Old Name");
    wallet.setAmountBalance(new Money(50, Currency.getInstance("EUR")));

    Wallet updatedWallet = new Wallet();
    updatedWallet.setId(walletId);
    updatedWallet.setName("Updated Wallet");
    updatedWallet.setAmountBalance(new Money(100, Currency.getInstance("EUR")));

    when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
    when(walletRepository.save(wallet)).thenReturn(updatedWallet);
    when(walletMapper.convertToUpdateDto(updatedWallet)).thenReturn(new WalletUpdateDto());

    WalletUpdateDto result = walletService.update(walletId, walletUpdateDto);

    assertNotNull(result);
    assertEquals("Updated Wallet", updatedWallet.getName());
    assertEquals(new Money(100, Currency.getInstance("EUR")), updatedWallet.getAmountBalance());
    verify(walletRepository).findById(walletId);
    verify(walletRepository).save(wallet);
    verify(walletMapper).convertToUpdateDto(updatedWallet);
  }

  @Test
  void testCreate() {
    Long userId = 1L;
    WalletCreateDto walletCreateDto = new WalletCreateDto();
    walletCreateDto.setUserId(userId);

    User user = new User();
    Wallet wallet = new Wallet();
    wallet.setUser(user);
    WalletCreateDto expectedWalletCreateDto = new WalletCreateDto();
    expectedWalletCreateDto.setUserId(userId);

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(walletMapper.convertCreateDtoToEntity(walletCreateDto, user)).thenReturn(wallet);
    when(walletMapper.convertToCreateDto(wallet)).thenReturn(expectedWalletCreateDto);
    when(walletRepository.save(wallet)).thenReturn(wallet);

    WalletCreateDto result = walletService.create(walletCreateDto);

    assertNotNull(result);
    verify(userRepository).findById(userId);
    verify(walletRepository).save(wallet);
    verify(walletMapper).convertToCreateDto(wallet);
  }

  @Test
  void testDelete() {
    Long walletId = 1L;
    Wallet wallet = new Wallet();
    wallet.setId(walletId);

    when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

    boolean result = walletService.delete(walletId);

    assertTrue(result);
    verify(walletRepository).delete(wallet);
  }

  @Test
  void testIsWalletOwnedByUser() {
    Long walletId = 1L;
    Long userId = 1L;

    Wallet wallet = new Wallet();
    User user = new User();
    user.setId(userId);
    wallet.setUser(user);

    when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

    boolean result = walletService.isWalletOwnedByUser(walletId, userId);

    assertTrue(result);
    verify(walletRepository).findById(walletId);
  }
}
