package com.pecunia.api.service;

import com.pecunia.api.dto.wallet.WalletCreateDto;
import com.pecunia.api.dto.wallet.WalletDto;
import com.pecunia.api.exception.ResourceNotFoundException;
import com.pecunia.api.mapper.WalletMapper;
import com.pecunia.api.model.Wallet;
import com.pecunia.api.repository.WalletRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/** WalletService. */
@Service
public class WalletService {

  private final WalletRepository walletRepository;
  private final WalletMapper walletMapper;

  /**
   * Constructor of walletService class.
   *
   * @param walletRepository wallet repository
   * @param walletMapper wallet mapper
   */
  public WalletService(WalletRepository walletRepository, WalletMapper walletMapper) {
    this.walletRepository = walletRepository;
    this.walletMapper = walletMapper;
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
   * @param walletCreateDto wallet Create Dto
   * @return new wallet
   */
  public WalletDto create(WalletCreateDto walletCreateDto) {
    Wallet wallet = walletMapper.convertToEntity(walletCreateDto);
    Wallet savedWallet = walletRepository.save(wallet);
    return walletMapper.convertToDto(savedWallet);
  }

  /**
   * Update a wallet.
   *
   * @param id wallet id
   * @param walletDetails wallet entity
   * @return updated wallet
   */
  public WalletDto update(Long id, Wallet walletDetails) {
    Wallet wallet = getWalletByIdOrThrow(id);
    if (wallet == null) {
      return null;
    }
    wallet.setName(walletDetails.getName());
    wallet.setAmount(walletDetails.getAmount());
    wallet.setCurrency(walletDetails.getCurrency());
    Wallet updateWallet = walletRepository.save(wallet);
    return walletMapper.convertToDto(updateWallet);
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
    walletRepository.delete(wallet);
    return true;
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
