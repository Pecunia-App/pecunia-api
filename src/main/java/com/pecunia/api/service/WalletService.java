package com.pecunia.api.service;

import com.pecunia.api.dto.transaction.TransactionDto;
import com.pecunia.api.dto.wallet.WalletCreateDto;
import com.pecunia.api.dto.wallet.WalletDto;
import com.pecunia.api.dto.wallet.WalletUpdateDto;
import com.pecunia.api.exception.ResourceNotFoundException;
import com.pecunia.api.mapper.TransactionMapper;
import com.pecunia.api.mapper.WalletMapper;
import com.pecunia.api.model.Transaction;
import com.pecunia.api.model.User;
import com.pecunia.api.model.Wallet;
import com.pecunia.api.repository.TransactionRepository;
import com.pecunia.api.repository.UserRepository;
import com.pecunia.api.repository.WalletRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/** WalletService. */
@Service
public class WalletService {

  private final WalletRepository walletRepository;
  private final WalletMapper walletMapper;
  private final UserRepository userRepository;
  private final TransactionRepository transactionRepository;
  private final TransactionMapper transactionMapper;

  /**
   * Constructor of walletService class.
   *
   * @param walletRepository wallet repository
   * @param walletMapper wallet mapper
   */
  public WalletService(
      WalletRepository walletRepository,
      WalletMapper walletMapper,
      UserRepository userRepository,
      TransactionRepository transactionRepository,
      TransactionMapper transactionMapper) {
    this.walletRepository = walletRepository;
    this.walletMapper = walletMapper;
    this.userRepository = userRepository;
    this.transactionRepository = transactionRepository;
    this.transactionMapper = transactionMapper;
  }

  /**
   * List all Wallet for Admin only.
   *
   * @return return list of walletDto
   */
  public List<WalletDto> getAllWallets() {
    List<Wallet> wallets = walletRepository.findAll();
    return wallets.stream().map(walletMapper::convertToDto).collect(Collectors.toList());
  }

  /**
   * Pagination of all transactions.
   *
   * @param walletId wallet id
   * @param pageable ex: page=0&size=20&sort=createdAt,desc
   * @return page transactionDto
   */
  public Page<TransactionDto> getTransactionsWallet(
      Long walletId, @ParameterObject Pageable pageable) {
    if (!walletRepository.existsById(walletId)) {
      throw new EntityNotFoundException("wallet non trouvé avec l'ID: " + walletId);
    }
    Page<Transaction> transactions = transactionRepository.findByWalletId(walletId, pageable);

    return transactions.map(transactionMapper::convertToDto);
  }

  /**
   * Get a Wallet by Id.
   *
   * @param id wallet id
   * @return wallet
   */
  public WalletDto getWalletById(Long id) {
    Wallet wallet = getWalletByIdOrThrow(id);
    return wallet != null ? walletMapper.convertToDto(wallet) : null;
  }

  /**
   * Create a Wallet.
   *
   * @param walletCreateDto wallet creation Dto
   * @return new wallet
   */
  public WalletCreateDto create(WalletCreateDto walletCreateDto) {
    if (walletCreateDto.getUserId() == null) {
      throw new IllegalArgumentException("UserId cannot be null");
    }
    User user =
        userRepository
            .findById(walletCreateDto.getUserId())
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
    Wallet wallet = walletMapper.convertCreateDtoToEntity(walletCreateDto, user);
    Wallet savedWallet = walletRepository.save(wallet);
    return walletMapper.convertToCreateDto(savedWallet);
  }

  /**
   * Update a wallet.
   *
   * @param id wallet id
   * @param walletUpdateDto wallet entity
   * @return updated wallet
   */
  public WalletUpdateDto update(Long id, WalletUpdateDto walletUpdateDto) {
    Wallet wallet = getWalletByIdOrThrow(id);
    if (walletUpdateDto.getName() != null) {
      wallet.setName(walletUpdateDto.getName().trim());
    }
    if (walletUpdateDto.getAmount() != null) {
      wallet.setAmountBalance(walletUpdateDto.getAmount());
    }
    Wallet updateWallet = walletRepository.save(wallet);
    return walletMapper.convertToUpdateDto(updateWallet);
  }

  /**
   * Delete a wallet.
   *
   * @param id wallet id
   * @return true if wallet has been deleted
   */
  public boolean delete(Long id) {
    Wallet wallet = getWalletByIdOrThrow(id);
    if (wallet == null) {
      return false;
    }
    if (wallet.getUser() != null) {
      wallet.getUser().setWallet(null);
      wallet.setUser(null);
    }
    walletRepository.delete(wallet);
    return true;
  }

  /**
   * Method securities.
   *
   * @param walletId wallet id
   * @param userId wallet's user id
   * @return boolean
   */
  public boolean isWalletOwnedByUser(Long walletId, Long userId) {
    Wallet wallet = walletRepository.findById(walletId).orElse(null);
    return wallet != null && wallet.getUser().getId().equals(userId);
  }

  private Wallet getWalletByIdOrThrow(Long id) {
    Wallet wallet =
        walletRepository
            .findById(id)
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "le Wallet avec l'id " + id + " n'a pas été trouvé."));
    return wallet;
  }
}
