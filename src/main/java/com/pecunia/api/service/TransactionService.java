package com.pecunia.api.service;

import com.pecunia.api.dto.transaction.TransactionCreateDto;
import com.pecunia.api.dto.transaction.TransactionDto;
import com.pecunia.api.dto.transaction.TransactionUpdateDto;
import com.pecunia.api.exception.ResourceNotFoundException;
import com.pecunia.api.exception.TransactionTypeNotSupportedException;
import com.pecunia.api.mapper.TransactionMapper;
import com.pecunia.api.model.Money;
import com.pecunia.api.model.Transaction;
import com.pecunia.api.model.TransactionType;
import com.pecunia.api.model.Wallet;
import com.pecunia.api.repository.TransactionRepository;
import com.pecunia.api.repository.WalletRepository;
import java.time.LocalDateTime;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/** TransactionService. */
@Service
public class TransactionService {
  private final TransactionRepository transactionRepository;
  private final TransactionMapper transactionMapper;
  private final WalletRepository walletRepository;

  /**
   * Repository constructor.
   *
   * @param transactionRepository params
   */
  public TransactionService(
      TransactionRepository transactionRepository,
      TransactionMapper transactionMapper,
      WalletRepository walletRepository) {
    this.transactionRepository = transactionRepository;
    this.transactionMapper = transactionMapper;
    this.walletRepository = walletRepository;
  }

  /**
   * Methode renvoyant tous les wallets disponbles, utiles aux admins.
   *
   * @param pageable PageSize, pageNumber, totalElements, totalPages
   * @return pagination de transactions
   */
  public Page<TransactionDto> getAll(@ParameterObject Pageable pageable) {
    Page<Transaction> transactions = transactionRepository.findAll(pageable);
    return transactions.map(transactionMapper::convertToDto);
  }

  /**
   * A transaction by Id.
   *
   * @param transactionId transaction id
   * @return a single transaction.
   */
  public TransactionDto getTransactionById(Long transactionId) {
    Transaction transaction = getTransactionByIdOrThrow(transactionId);
    return transaction != null ? transactionMapper.convertToDto(transaction) : null;
  }

  private static void validateTransaction(TransactionCreateDto transactionCreateDto) {
    if (transactionCreateDto.getWalletId() == null) {
      throw new IllegalArgumentException("WalletId cannot be null.");
    }
    if (transactionCreateDto.getType() == null) {
      throw new TransactionTypeNotSupportedException("Transaction Type cannot be null.");
    }
  }

  /**
   * Create a transaction with balance update.
   *
   * @param transactionCreateDto transaction creation dto
   * @return new transaction
   */
  public TransactionCreateDto create(TransactionCreateDto transactionCreateDto) {
    validateTransaction(transactionCreateDto);
    Wallet wallet =
        walletRepository
            .findById(transactionCreateDto.getWalletId())
            .orElseThrow(() -> new IllegalArgumentException("Wallet not found"));
    Transaction transaction =
        transactionMapper.convertCreateDtoToEntity(transactionCreateDto, wallet);
    Transaction savedTransaction = transactionRepository.save(transaction);
    updateWalletBalance(wallet, transactionCreateDto.getAmount(), transactionCreateDto.getType());
    return transactionMapper.convertToCreateDto(savedTransaction);
  }

  private void updateWalletBalance(Wallet wallet, Money amount, TransactionType type) {
    Money currentBalance = wallet.getAmountBalance();
    Money newBalance;

    switch (type) {
      case CREDIT:
        newBalance = currentBalance.add(amount);
        break;
      case DEBIT:
        newBalance = currentBalance.substract(amount);
        break;
      default:
        throw new IllegalArgumentException("Transaction type not supported: " + type);
    }

    wallet.setAmountBalance(newBalance);
    wallet.setUpdatedAt(LocalDateTime.now());
    walletRepository.save(wallet);
  }

  private Money reverseTransactionImpact(Money balance, Money amount, TransactionType type) {
    switch (type) {
      case CREDIT:
        return balance.substract(amount);
      case DEBIT:
        return balance.add(amount);
      default:
        throw new TransactionTypeNotSupportedException("Transaction type not supported: " + type);
    }
  }

  /**
   * Update a transaction and update balance from wallet.
   *
   * @param id transaction id
   * @param transactionUpdateDto {@link TransactionUpdateDto}
   * @return transationUpdateDto
   */
  public TransactionUpdateDto update(Long id, TransactionUpdateDto transactionUpdateDto) {
    Transaction transaction = getTransactionByIdOrThrow(id);

    final Money oldAmount = transaction.getAmount();
    final TransactionType oldType = transaction.getType();

    if (transactionUpdateDto.getAmount() != null) {
      transaction.setAmount(transactionUpdateDto.getAmount());
    }
    if (transaction.getNote() != null) {
      transaction.setNote(transactionUpdateDto.getNote());
    }
    if (transaction.getType() != null) {
      transaction.setType(transactionUpdateDto.getType());
    }
    transaction.setUpdatedAt(LocalDateTime.now());
    Wallet wallet =
        walletRepository
            .findById(transaction.getWallet().getId())
            .orElseThrow(() -> new ResourceNotFoundException("Wallet not found"));

    Transaction updatedTransaction = transactionRepository.save(transaction);
    Money balanceAfterReversal =
        reverseTransactionImpact(wallet.getAmountBalance(), oldAmount, oldType);
    wallet.setAmountBalance(balanceAfterReversal);

    updateWalletBalance(wallet, updatedTransaction.getAmount(), updatedTransaction.getType());

    return transactionMapper.convertToUpdateDto(updatedTransaction);
  }

  /**
   * delete transaction by id.
   *
   * @param id transaction id
   * @return boolean
   */
  public boolean delete(Long id) {
    Transaction transaction = getTransactionByIdOrThrow(id);
    if (transaction == null) {
      return false;
    }
    Wallet wallet =
        walletRepository
            .findById(transaction.getWallet().getId())
            .orElseThrow(() -> new ResourceNotFoundException("Wallet not found."));

    Money currentBalance = wallet.getAmountBalance();
    Money balanceAfterDeletion =
        reverseTransactionImpact(currentBalance, transaction.getAmount(), transaction.getType());
    wallet.setAmountBalance(balanceAfterDeletion);
    wallet.setUpdatedAt(LocalDateTime.now());

    transactionRepository.delete(transaction);
    return true;
  }

  /**
   * Security method to search the user of a Wallet and his transactions.
   *
   * @param transaction TransactionCreateDto object
   * @param userId user id of the wallet
   * @return boolean
   */
  public boolean isTransactionOwnedByWalletUser(TransactionCreateDto transaction, Long userId) {
    if (transaction.getWalletId() == null) {
      return false;
    }
    Wallet wallet = walletRepository.findById(transaction.getWalletId()).orElse(null);
    return wallet != null && wallet.getUser().getId().equals(userId);
  }

  /**
   * Security method to search the user of a Wallet and his transactions.
   *
   * @param transactionId transaction id
   * @param userId user id
   * @return boolean
   */
  public boolean isTransactionOwnedByWalletUserById(Long transactionId, Long userId) {
    Transaction transaction = getTransactionByIdOrThrow(transactionId);
    if (transaction == null) {
      return false;
    }
    Wallet wallet = transaction.getWallet();
    return wallet != null && wallet.getUser().getId().equals(userId);
  }

  private Transaction getTransactionByIdOrThrow(Long id) {
    Transaction transaction =
        transactionRepository
            .findById(id)
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "La transaction avec l'id" + id + " n'a pas été trouvé."));
    return transaction;
  }
}
