package com.pecunia.api.controller;

import com.pecunia.api.dto.provider.ProviderDto;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/providers")
public class ProviderController {
  @Autowired private final ProviderService providerService;

  @GetMapping
  @Operation()
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Page<ProviderDto>> getAllProviders(Pageable pageable) {

    Page<ProviderDto> providers = providerService.getAll(pageable);
    return providers.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(providers);
  }
}
