package com.pecunia.api.controller;

import com.pecunia.api.dto.provider.ProviderCreateDto;
import com.pecunia.api.dto.provider.ProviderDto;
import com.pecunia.api.dto.provider.ProviderUpdateDto;
import com.pecunia.api.security.CanAccessProvider;
import com.pecunia.api.security.HasRole;
import com.pecunia.api.service.ProviderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/** Provider Controller. */
@RestController
@Tag(name = "Provider Controller", description = "Handle CRUD Provider")
@RequestMapping("/providers")
public class ProviderController {
  @Autowired private ProviderService providerService;

  /**
   * Methode renvoyant tous les providers disponbles, utiles aux admins.
   *
   * @param pageable pagination
   * @return pagination de providers
   */
  @GetMapping
  @Operation(summary = "Return all providers", description = "Role admin require")
  @HasRole("ADMIN")
  public ResponseEntity<Page<ProviderDto>> getAllProviders(Pageable pageable) {
    Page<ProviderDto> providers = providerService.getAll(pageable);
    return providers.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(providers);
  }

  /**
   * Return all providers from an user.
   *
   * @param userId user i
   * @return noContent or OK
   */
  @GetMapping("/users/{userId}")
  @Operation(
      summary = "Return all providers from an user.",
      description = "Role Admin require or login with correct user id.")
  @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
  public ResponseEntity<List<ProviderDto>> getAllProvidersByUser(@PathVariable Long userId) {
    List<ProviderDto> providers = providerService.getUserProviders(userId);
    return providers.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(providers);
  }

  /**
   * Search by provider name.
   *
   * @param userId user id
   * @param name provider name LIKE
   * @param pageable pagination
   * @return pagination with provider name
   */
  @GetMapping("/users/{userId}/search-name")
  @Operation()
  @CanAccessProvider
  public ResponseEntity<Page<ProviderDto>> searchProviderName(
      Long userId, @RequestParam String name, Pageable pageable) {
    Page<ProviderDto> providers = providerService.searchProviders(userId, name, pageable);
    return providers.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(providers);
  }

  @GetMapping("/{id}")
  @Operation()
  @CanAccessProvider
  public ResponseEntity<ProviderDto> getProviderById(@PathVariable Long id) {
    ProviderDto provider = providerService.getProviderById(id);
    return provider == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(provider);
  }

  /**
   * Create a Provider.
   *
   * @param provider providerCreateDto
   * @return new provider or bad request
   */
  @PostMapping
  @Operation()
  @PreAuthorize("hasRole('ADMIN') or #provider.userId == authentication.principal.id")
  public ResponseEntity<ProviderCreateDto> createProvider(
      @Valid @RequestBody ProviderCreateDto provider) {
    ProviderCreateDto savedProvider = providerService.create(provider);
    return ResponseEntity.status(HttpStatus.CREATED).body(savedProvider);
  }

  /**
   * Modification d'un provider.
   *
   * @param providerId provider id
   * @param provider body à update
   * @return 200 ou 404
   */
  @PutMapping("/{id}")
  @Operation(
      summary = "Update a specific Provider by Id",
      description = "Role Admin require or login with correct user id.")
  @CanAccessProvider
  public ResponseEntity<ProviderUpdateDto> updateProvider(
      @PathVariable Long id, @Valid @RequestBody ProviderUpdateDto provider) {
    ProviderUpdateDto updatedProvider = providerService.update(id, provider);
    return updatedProvider == null
        ? ResponseEntity.notFound().build()
        : ResponseEntity.ok(updatedProvider);
  }

  @DeleteMapping("/{id}")
  @Operation(
      summary = "Delete a specific Provider by Id",
      description = "Role admin require or login with correct user id.")
  @CanAccessProvider
  public ResponseEntity<Void> deleteProvider(@PathVariable Long id) {
    return providerService.delete(id)
        ? ResponseEntity.noContent().build()
        : ResponseEntity.notFound().build();
  }
}
