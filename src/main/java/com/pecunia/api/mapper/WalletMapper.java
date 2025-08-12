package com.pecunia.api.mapper;

import com.pecunia.api.dto.wallet.WalletCreateDto;
import com.pecunia.api.dto.wallet.WalletDto;
import com.pecunia.api.dto.wallet.WalletUpdateDto;
import com.pecunia.api.model.User;
import com.pecunia.api.model.Wallet;
import org.springframework.stereotype.Component;

@Component
public class WalletMapper {

  public Wallet convertToEntity(WalletCreateDto dto) {
    Wallet wallet = new Wallet();
    wallet.setName(dto.getName());
    wallet.setAmountBalance(dto.getAmount());

    return wallet;
  }

  public Wallet convertCreateDtoToEntity(WalletCreateDto dto, User user) {
    Wallet wallet = new Wallet();
    wallet.setName(dto.getName());
    wallet.setAmountBalance(dto.getAmount());
    wallet.setUser(user);

    return wallet;
  }

  public WalletDto convertToDto(Wallet wallet) {

    WalletDto walletDto = new WalletDto();
    walletDto.setId(wallet.getId());
    walletDto.setName(wallet.getName());
    walletDto.setAmount(wallet.getAmountBalance());

    return walletDto;
  }

  public WalletUpdateDto convertToUpdateDto(Wallet wallet) {
    WalletUpdateDto walletUpdateDto = new WalletUpdateDto();
    walletUpdateDto.setName(wallet.getName());
    walletUpdateDto.setAmount(wallet.getAmountBalance());

    return walletUpdateDto;
  }

  public WalletCreateDto convertToCreateDto(Wallet wallet) {
    WalletCreateDto walletCreateDto = new WalletCreateDto();
    walletCreateDto.setName(wallet.getName());
    walletCreateDto.setAmount(wallet.getAmountBalance());
    walletCreateDto.setUserId(wallet.getUser().getId());

    return walletCreateDto;
  }
}
