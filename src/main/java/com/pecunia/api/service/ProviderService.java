package com.pecunia.api.service;

import com.pecunia.api.dto.provider.ProviderDto;
import com.pecunia.api.repository.ProviderRepository;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProviderService {
  private final ProviderRepository providerRepository;

  public ProviderService(ProviderRepository providerRepository) {
    this.providerRepository = providerRepository;
  }

  public Page<ProviderDto> getAll(@ParameterObject Pageable pageable) {
    Page<ProviderDto> providers = providerRepository.findAll(pageable);
    return providers.map(providerMapper::convertToDto);
  }
}
