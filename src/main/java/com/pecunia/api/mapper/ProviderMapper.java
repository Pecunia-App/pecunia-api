package com.pecunia.api.mapper;

import com.pecunia.api.dto.provider.ProviderCreateDto;
import com.pecunia.api.dto.provider.ProviderDto;
import com.pecunia.api.dto.provider.ProviderUpdateDto;
import com.pecunia.api.model.Provider;
import com.pecunia.api.model.User;
import org.springframework.stereotype.Component;

@Component
public class ProviderMapper {
  public ProviderDto convertToDto(Provider provider) {
    ProviderDto dto = new ProviderDto();
    dto.setId(provider.getId());
    dto.setProviderName(provider.getProviderName());
    return dto;
  }

  public Provider convertCreateDtoToEntity(ProviderCreateDto dto, User user) {
    Provider entity = new Provider();
    entity.setProviderName(dto.getProviderName());
    entity.setUser(user);
    return entity;
  }

  public ProviderCreateDto convertToCreateDto(Provider provider) {
    ProviderCreateDto dto = new ProviderCreateDto();
    dto.setProviderName(provider.getProviderName());
    dto.setUserId(provider.getUser().getId());
    return dto;
  }

  public ProviderUpdateDto convertToUpdateDto(Provider provider) {
    ProviderUpdateDto dto = new ProviderUpdateDto();
    dto.setProviderName(provider.getProviderName());
    return dto;
  }
}
