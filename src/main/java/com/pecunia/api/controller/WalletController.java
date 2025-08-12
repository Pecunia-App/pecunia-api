package com.pecunia.api.controller;

import com.pecunia.api.dto.wallet.WalletCreateDto;
import com.pecunia.api.dto.wallet.WalletDto;
import com.pecunia.api.dto.wallet.WalletUpdateDto;
import com.pecunia.api.service.WalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** WalletController. */
@RestController
@Tag(name = "Wallet Controller", description = "Handle CRUD Wallet")
@RequestMapping("/wallets")
public class WalletController {

  @Autowired private WalletService walletService;

  /**
   * Methode renvoyant tous les wallets disponbles, utiles aux admins.
   *
   * @return une liste de wallet
   */
  @GetMapping()
  @Operation(summary = "Return all wallets", description = "Role Admin Require")
  public ResponseEntity<List<WalletDto>> getAllWallets() {
    List<WalletDto> wallets = walletService.getAllWallets();
    return wallets.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(wallets);
  }

  /**
   * Retour d'un wallet par son identifiant.
   *
   * @param id -> Identifiant d'un wallet
   * @return 200 OU 404
   */
  @GetMapping("/{id}")
  @Operation(
      summary = "Get a specific Wallet by id",
      description = "Role Admin require or login with correct user id")
  public ResponseEntity<WalletDto> getWalletById(@PathVariable Long id) {
    WalletDto wallet = walletService.getWalletById(id);
    return wallet == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(wallet);
  }

  /**
   * Modification d'un wallet.
   *
   * @param id Identifiant d'un wallet
   * @param wallet Body à update
   * @return 200 OU 404
   */
  @PutMapping("/{id}")
  @Operation(
      summary = "Update a specific Wallet by Id",
      description = "Role Admin require or login with correct user id.")
  public ResponseEntity<WalletUpdateDto> updateWallet(
      @PathVariable Long id, @Valid @RequestBody WalletUpdateDto wallet) {
    WalletUpdateDto updateWallet = walletService.update(id, wallet);
    return updateWallet == null
        ? ResponseEntity.notFound().build()
        : ResponseEntity.ok(updateWallet);
  }

  /**
   * Creation d'un wallet en meme temps que la creation d'un {@code User}.
   *
   * @param wallet Body pour la création
   * @returnSTATUS 201
   */
  @PostMapping()
  @Operation(
      summary = "Create a new wallet link by an user",
      description = "Role Admin require or user who create a new account.")
  public ResponseEntity<WalletCreateDto> createWallet(@Valid @RequestBody WalletCreateDto wallet) {
    WalletCreateDto savedWallet = walletService.create(wallet);
    return ResponseEntity.status(HttpStatus.CREATED).body(savedWallet);
  }

  /**
   * Request to delete a wallet and his transactions.
   *
   * @param id d'un wallet à supprimer
   * @return Soit une 204 ou une 404
   */
  @DeleteMapping("/{id}")
  @Operation()
  public ResponseEntity<Void> deleteWallet(@PathVariable Long id) {
    return walletService.delete(id)
        ? ResponseEntity.noContent().build()
        : ResponseEntity.notFound().build();
  }
}
