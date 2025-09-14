package com.pecunia.api.mapper;

import com.pecunia.api.dto.tag.TagDto;
import com.pecunia.api.dto.transaction.TransactionCreateDto;
import com.pecunia.api.dto.transaction.TransactionDto;
import com.pecunia.api.dto.transaction.TransactionUpdateDto;
import com.pecunia.api.model.Provider;
import com.pecunia.api.model.Tag;
import com.pecunia.api.model.Transaction;
import com.pecunia.api.model.Wallet;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/** TransactionMapper. */
@Component
public class TransactionMapper {
  private final TagMapper tagMapper;

  public TransactionMapper(TagMapper tagMapper) {
    this.tagMapper = tagMapper;
  }

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
    if (transaction.getTags() != null) {
      Set<TagDto> tagDtos =
          transaction.getTags().stream().map(tagMapper::convertToDto).collect(Collectors.toSet());
      dto.setTags(tagDtos);
    }
    dto.setProvider(transaction.getProvider());

    return dto;
  }

  public Transaction convertCreateDtoToEntity(
      TransactionCreateDto dto, Wallet wallet, Set<Tag> tags, Provider provider) {
    Transaction transaction = new Transaction();
    transaction.setAmount(dto.getAmount());
    transaction.setNote(dto.getNote());
    transaction.setType(dto.getType());
    transaction.setTags(tags);
    transaction.setWallet(wallet);
    if (transaction.getProvider() != null) {
      transaction.setProvider(provider);
    }
    transaction.setProvider(null);

    return transaction;
  }

  public TransactionCreateDto convertToCreateDto(Transaction transaction) {
    TransactionCreateDto dto = new TransactionCreateDto();
    dto.setAmount(transaction.getAmount());
    dto.setNote(transaction.getNote());
    dto.setType(transaction.getType());
    dto.setWalletId(transaction.getWallet().getId());
    if (transaction.getTags() != null) {
      Set<Long> tagsIds =
          transaction.getTags().stream().map(Tag::getId).collect(Collectors.toSet());
      dto.setTagsIds(tagsIds);
    }
    dto.setProviderId(transaction.getProvider().getId());

    return dto;
  }

  public TransactionUpdateDto convertToUpdateDto(Transaction transaction) {
    TransactionUpdateDto dto = new TransactionUpdateDto();
    dto.setAmount(transaction.getAmount());
    dto.setNote(transaction.getNote());
    dto.setType(transaction.getType());
    dto.setCreatedAt(transaction.getCreatedAt());
    if (transaction.getTags() != null) {
      Set<Long> tagsIds =
          transaction.getTags().stream().map(Tag::getId).collect(Collectors.toSet());
      dto.setTagsIds(tagsIds);
    }
    dto.setProviderId(transaction.getProvider().getId());

    return dto;
  }

  public Transaction convertToEntiy(TransactionCreateDto dto) {
    Transaction transaction = new Transaction();
    transaction.setAmount(dto.getAmount());
    transaction.setNote(dto.getNote());
    transaction.setType(dto.getType());

    return transaction;
  }
}
