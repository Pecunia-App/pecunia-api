package com.pecunia.api.mapper;

import com.pecunia.api.dto.wallet.WalletCreateDto;
import com.pecunia.api.dto.wallet.WalletDto;
import com.pecunia.api.dto.wallet.WalletUpdateDto;
import com.pecunia.api.model.Wallet;
import org.springframework.stereotype.Component;

/** WalletMapper. */
@Component
public class WalletMapper {
  /**
   * Convert Wallet Create Dto to Wallet Entity.
   *
   * @param dto wallet create dto.
   * @return wallet entity
   */
  public Wallet convertToEntity(WalletCreateDto dto) {
    Wallet wallet = new Wallet();
    wallet.setName(dto.getName());
    wallet.setAmount(dto.getAmount());
    wallet.setCurrency(dto.getCurrency());

    return wallet;
  }

  /**
   * Convert Wallet Update Dto to Wallet Entity.
   *
   * @param dto wallet update dto.
   * @return wallet entity
   */
  public Wallet convertUpdateDtoToEntity(WalletUpdateDto dto) {
    Wallet wallet = new Wallet();
    wallet.setName(dto.getName());
    wallet.setCurrency(dto.getCurrency());
    wallet.setAmount(dto.getAmount());
    return wallet;
  }

  /**
   * Convert Wallet Entity to Create Dto.
   *
   * @param wallet wallet entity
   * @return wallet create Dto
   */
  public WalletCreateDto convertToCreateDto(Wallet wallet) {
    WalletCreateDto dto = new WalletCreateDto();
    dto.setName(wallet.getName());
    dto.setAmount(wallet.getAmount());
    dto.setCurrency(wallet.getCurrency());
    dto.setUserId(wallet.getId());
    return dto;
  }

  /**
   * Convert Wallet Entity to simple Dto.
   *
   * @param wallet wallet entity
   * @return dto wallet dto
   */
  public WalletDto convertToDto(Wallet wallet) {
    WalletDto dto = new WalletDto();
    dto.setId(wallet.getId());
    dto.setName(wallet.getName());
    dto.setAmount(wallet.getAmount());
    dto.setCurrency(wallet.getCurrency());
    return dto;
  }

  /**
   * Convert Wallet Entity to Update Dto.
   *
   * @param wallet wallet entity
   * @return update wallet dto
   */
  public WalletUpdateDto convertToUpdateDto(Wallet wallet) {
    WalletUpdateDto dto = new WalletUpdateDto();
    dto.setName(wallet.getName());
    dto.setAmount(wallet.getAmount());
    dto.setCurrency(wallet.getCurrency());
    return dto;
  }
}
