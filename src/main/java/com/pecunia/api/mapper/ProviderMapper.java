package com.pecunia.api.mapper;

import com.pecunia.api.dto.provider.ProviderDto;
import com.pecunia.api.model.Provider;
import org.springframework.stereotype.Component;

@Component
public class ProviderMapper {
  public ProviderDto convertToDto(Provider provider) {
    ProviderDto dto = new ProviderDto();
    dto.setId(provider.getId());
    dto.setProviderName(provider.getProviderName());
    return dto;
  }
}
