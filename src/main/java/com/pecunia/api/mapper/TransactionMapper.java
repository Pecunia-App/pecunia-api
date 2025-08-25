package com.pecunia.api.mapper;

import com.pecunia.api.dto.transaction.TransactionCreateDto;
import com.pecunia.api.dto.transaction.TransactionDto;
import com.pecunia.api.dto.transaction.TransactionUpdateDto;
import com.pecunia.api.model.Transaction;
import com.pecunia.api.model.Wallet;
import org.springframework.stereotype.Component;

/** TransactionMapper. */
@Component
public class TransactionMapper {

  /**
   * @param transaction
   * @return
   */
  public TransactionDto convertToDto(Transaction transaction) {
    TransactionDto dto = new TransactionDto();
    dto.setId(transaction.getId());
    dto.setNote(transaction.getNote());
    dto.setAmount(transaction.getAmount());
    dto.setType(transaction.getType());
    dto.setCreatedAt(transaction.getCreatedAt());
    dto.setUpdatedAt(transaction.getUpdatedAt());

    return dto;
  }

  public Transaction convertCreateDtoToEntity(TransactionCreateDto dto, Wallet wallet) {
    Transaction transaction = new Transaction();
    transaction.setAmount(dto.getAmount());
    transaction.setNote(dto.getNote());
    transaction.setType(dto.getType());
    transaction.setWallet(wallet);

    return transaction;
  }

  public TransactionCreateDto convertToCreateDto(Transaction transaction) {
    TransactionCreateDto dto = new TransactionCreateDto();
    dto.setAmount(transaction.getAmount());
    dto.setNote(transaction.getNote());
    dto.setType(transaction.getType());
    dto.setWalletId(transaction.getWallet().getId());

    return dto;
  }

  public TransactionUpdateDto convertToUpdateDto(Transaction transaction) {
    TransactionUpdateDto transactionUpdateDto = new TransactionUpdateDto();
    transactionUpdateDto.setAmount(transaction.getAmount());
    transactionUpdateDto.setNote(transaction.getNote());
    transactionUpdateDto.setType(transaction.getType());
    transactionUpdateDto.setCreatedAt(transaction.getCreatedAt());

    return transactionUpdateDto;
  }

  public Transaction convertToEntiy(TransactionCreateDto dto) {
    Transaction transaction = new Transaction();
    transaction.setAmount(dto.getAmount());
    transaction.setNote(dto.getNote());
    transaction.setType(dto.getType());

    return transaction;
  }
}
