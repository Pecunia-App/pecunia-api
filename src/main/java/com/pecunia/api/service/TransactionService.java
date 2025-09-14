package com.pecunia.api.service;

import com.pecunia.api.dto.transaction.TransactionCreateDto;
import com.pecunia.api.dto.transaction.TransactionDto;
import com.pecunia.api.dto.transaction.TransactionUpdateDto;
import com.pecunia.api.exception.CategoryTypeNotSupportedException;
import com.pecunia.api.exception.ResourceNotFoundException;
import com.pecunia.api.mapper.TransactionMapper;
import com.pecunia.api.model.Category;
import com.pecunia.api.model.CategoryType;
import com.pecunia.api.model.Money;
import com.pecunia.api.model.Provider;
import com.pecunia.api.model.Tag;
import com.pecunia.api.model.Transaction;
import com.pecunia.api.model.Wallet;
import com.pecunia.api.repository.CategoryRepository;
import com.pecunia.api.repository.ProviderRepository;
import com.pecunia.api.repository.TagRepository;
import com.pecunia.api.repository.TransactionRepository;
import com.pecunia.api.repository.WalletRepository;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/** TransactionService. */
@Service
public class TransactionService {
  @Autowired private TransactionRepository transactionRepository;
  @Autowired private TransactionMapper transactionMapper;
  @Autowired private WalletRepository walletRepository;
  @Autowired private TagRepository tagRepository;
  @Autowired private ProviderRepository providerRepository;

  /**
   * Methode renvoyant toutes les transactions disponibles, utiles aux admins.
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

    if (transactionCreateDto.getCategoryId() == null) {
      throw new IllegalArgumentException("categoryId cannot be null.");
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
    final Wallet wallet =
        walletRepository
            .findById(transactionCreateDto.getWalletId())
            .orElseThrow(() -> new IllegalArgumentException("Wallet not found"));
    Set<Tag> tags = new HashSet<>();
    if (transactionCreateDto.getTagsIds() != null) {
      tags = new HashSet<>(tagRepository.findAllById(transactionCreateDto.getTagsIds()));

      if (tags.size() != transactionCreateDto.getTagsIds().size()) {
        throw new IllegalArgumentException("Certains tags n'existents pas.");
      }
    }
    Provider provider = null;
    if (transactionCreateDto.getProviderId() != null) {
      provider =
          providerRepository
              .findById(transactionCreateDto.getProviderId())
              .orElseThrow(() -> new IllegalArgumentException("Provider not found"));
    }
    Category category =
        categoryRepository
            .findById(transactionCreateDto.getCategoryId())
            .orElseThrow(() -> new IllegalArgumentException("Category not found."));

    Transaction transaction =
        transactionMapper.convertCreateDtoToEntity(
            transactionCreateDto, wallet, tags, provider, category);
    Transaction savedTransaction = transactionRepository.save(transaction);
    updateWalletBalance(wallet, transactionCreateDto.getAmount(), category.getType());
    return transactionMapper.convertToCreateDto(savedTransaction);
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
    final CategoryType oldType = transaction.getCategoryType();

    if (transactionUpdateDto.getAmount() != null) {
      transaction.setAmount(transactionUpdateDto.getAmount());
    }
    if (transactionUpdateDto.getNote() != null) {
      transaction.setNote(transactionUpdateDto.getNote());
    }
    if (transactionUpdateDto.getCreatedAt() != null) {
      transaction.setCreatedAt(transactionUpdateDto.getCreatedAt());
    }
    Set<Tag> newTags = new HashSet<>();
    if (transactionUpdateDto.getTagsIds() != null) {
      newTags = new HashSet<>(tagRepository.findAllById(transactionUpdateDto.getTagsIds()));
      if (newTags.size() != transactionUpdateDto.getTagsIds().size()) {
        throw new IllegalArgumentException("Certains tags n'existent pas.");
      }
      transaction.setTags(newTags);
    }
    Provider newProvider = null;
    if (transactionUpdateDto.getProviderId() != null) {
      newProvider =
          providerRepository
              .findById(transactionUpdateDto.getProviderId())
              .orElseThrow(() -> new IllegalArgumentException("Provider n'existe pas."));
      transaction.setProvider(newProvider);
    }

    if (transactionUpdateDto.getCategoryId() != null) {
      Category newCategory =
          categoryRepository
              .findById(transactionUpdateDto.getCategoryId())
              .orElseThrow(() -> new IllegalArgumentException("Category doesnt exist."));
      transaction.setCategory(newCategory);
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

    updateWalletBalance(
        wallet, updatedTransaction.getAmount(), updatedTransaction.getCategoryType());

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
        reverseTransactionImpact(
            currentBalance, transaction.getAmount(), transaction.getCategoryType());
    wallet.setAmountBalance(balanceAfterDeletion);
    wallet.setUpdatedAt(LocalDateTime.now());

    transactionRepository.delete(transaction);
    return true;
  }

  private void updateWalletBalance(Wallet wallet, Money amount, CategoryType type) {
    Money currentBalance = wallet.getAmountBalance();
    Money newBalance;

    switch (type) {
      case CREDIT:
        newBalance = currentBalance.add(amount);
        break;
      case DEBIT:
        newBalance = currentBalance.subtract(amount);
        break;
      default:
        throw new IllegalArgumentException("Transaction type not supported: " + type);
    }

    wallet.setAmountBalance(newBalance);
    wallet.setUpdatedAt(LocalDateTime.now());

    walletRepository.save(wallet);
  }

  private Money reverseTransactionImpact(Money balance, Money amount, CategoryType type) {
    switch (type) {
      case CREDIT:
        return balance.subtract(amount);
      case DEBIT:
        return balance.add(amount);
      default:
        throw new CategoryTypeNotSupportedException("Category type not supported: " + type);
    }
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
                        "La transaction avec l'id " + id + " n'a pas été trouvé."));
    return transaction;
  }

  public void recalculateWalletBalancesForCategoryTypeChange(
      Long categoryId, CategoryType oldType, CategoryType newType) {
    List<Transaction> transactions = transactionRepository.findByCategoryId(categoryId);

    for (Transaction transaction : transactions) {
      Wallet wallet = transaction.getWallet();
      Money amount = transaction.getAmount();

      Money balanceAfterReversal =
          reverseTransactionImpact(wallet.getAmountBalance(), amount, oldType);
      wallet.setAmountBalance(balanceAfterReversal);
      updateWalletBalance(wallet, amount, newType);
    }
  }
}
