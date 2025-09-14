package com.pecunia.api.service;

import com.pecunia.api.dto.provider.ProviderCreateDto;
import com.pecunia.api.dto.provider.ProviderDto;
import com.pecunia.api.dto.provider.ProviderUpdateDto;
import com.pecunia.api.exception.ResourceNotFoundException;
import com.pecunia.api.mapper.ProviderMapper;
import com.pecunia.api.model.Provider;
import com.pecunia.api.model.Transaction;
import com.pecunia.api.model.User;
import com.pecunia.api.repository.ProviderRepository;
import com.pecunia.api.repository.UserRepository;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProviderService {
  private final ProviderRepository providerRepository;
  private final ProviderMapper providerMapper;
  private final UserRepository userRepository;

  public ProviderService(
      ProviderRepository providerRepository,
      ProviderMapper providerMapper,
      UserRepository userRepository) {
    this.providerRepository = providerRepository;
    this.providerMapper = providerMapper;
    this.userRepository = userRepository;
  }

  public Page<ProviderDto> getAll(@ParameterObject Pageable pageable) {
    Page<Provider> providers = providerRepository.findAll(pageable);
    return providers.map(providerMapper::convertToDto);
  }

  public Page<ProviderDto> getUserProviders(Long userId, Pageable pageable) {
    Page<Provider> providers = providerRepository.findByUserId(userId, pageable);
    return providers.map(providerMapper::convertToDto);
  }

  public Page<ProviderDto> searchProviders(Long userId, String searchTerm, Pageable pageable) {
    Page<Provider> providers =
        providerRepository.findByProviderNameContainingIgnoreCaseAndTransactionsWalletUserId(
            searchTerm, userId, pageable);
    return providers.map(providerMapper::convertToDto);
  }

  public ProviderDto getProviderById(Long providerId) {
    Provider provider = getProviderByIdOrThrow(providerId);
    return provider != null ? providerMapper.convertToDto(provider) : null;
  }

  public ProviderCreateDto create(ProviderCreateDto providerCreateDto) {
    validateProviderCreation(providerCreateDto);
    User user =
        userRepository
            .findById(providerCreateDto.getUserId())
            .orElseThrow(() -> new IllegalArgumentException("User not found."));
    Provider provider = providerMapper.convertCreateDtoToEntity(providerCreateDto, user);
    Provider savedProvider = providerRepository.save(provider);
    return providerMapper.convertToCreateDto(savedProvider);
  }

  public ProviderUpdateDto update(Long providerId, ProviderUpdateDto providerUpdateDto) {
    Provider provider = getProviderByIdOrThrow(providerId);
    if (providerUpdateDto.getProviderName() != null) {
      provider.setProviderName(providerUpdateDto.getProviderName().trim());
    }
    Provider updatedProvider = providerRepository.save(provider);
    return providerMapper.convertToUpdateDto(updatedProvider);
  }

  public boolean delete(Long providerId) {
    Provider provider = getProviderByIdOrThrow(providerId);
    if (provider == null) {
      return false;
    }
    if (provider.getTransactions() != null) {
      for (Transaction transaction : provider.getTransactions()) {
        transaction.setProvider(null);
      }
      provider.getTransactions().clear();
    }
    providerRepository.delete(provider);
    return true;
  }

  private Provider getProviderByIdOrThrow(Long providerId) {
    return providerRepository
        .findById(providerId)
        .orElseThrow(
            () ->
                new ResourceNotFoundException(
                    "Le provider avec l'id " + providerId + " n'a pas été trouvé."));
  }

  private static void validateProviderCreation(ProviderCreateDto providerRequestDto) {
    if (providerRequestDto.getProviderName() == null) {
      throw new IllegalArgumentException("Provider name cannot be null.");
    }
  }
}
