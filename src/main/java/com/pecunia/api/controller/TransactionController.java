package com.pecunia.api.controller;

import com.pecunia.api.dto.transaction.TransactionCreateDto;
import com.pecunia.api.dto.transaction.TransactionDto;
import com.pecunia.api.dto.transaction.TransactionUpdateDto;
import com.pecunia.api.security.CanAccessTransaction;
import com.pecunia.api.security.HasRole;
import com.pecunia.api.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.web.bind.annotation.RestController;

/** TransactionController. */
@RestController
@RequestMapping("/transactions")
public class TransactionController {
  @Autowired private TransactionService transactionService;

  /**
   * Toutes les transactions disponibles, utiles aux admins.
   *
   * @param pageable {@link org.springframework.data.domain.PageRequest}
   * @return page of transactionDto
   */
  @GetMapping
  @Operation(summary = "Return all transaction wih pagination", description = "Role Admin require")
  @HasRole("ADMIN")
  public ResponseEntity<Page<TransactionDto>> getAllTransactions(Pageable pageable) {
    Page<TransactionDto> transactions = transactionService.getAll(pageable);
    return transactions.isEmpty()
        ? ResponseEntity.noContent().build()
        : ResponseEntity.ok(transactions);
  }

  /**
   * Get a specific transaction by id.
   *
   * @param id transaction id
   * @return one transaction
   */
  @GetMapping("/{id}")
  @Operation(
      summary = "Get a specific Transaction by id",
      description = "Role Admin require or login with correct user id")
  @CanAccessTransaction
  public ResponseEntity<TransactionDto> getTransactionById(@PathVariable Long id) {
    TransactionDto transaction = transactionService.getTransactionById(id);
    return transaction == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(transaction);
  }

  /**
   * Request to create a transaction.
   *
   * @param transaction
   * @return
   */
  @PostMapping()
  @Operation(
      summary = "Create a new transaction.",
      description = "Role admin or user whos created wallet")
  @PreAuthorize(
      "@transactionService.isTransactionOwnedByWalletUser(#transaction,"
          + " authentication.principal.id) or hasRole('ROLE_ADMIN')")
  public ResponseEntity<TransactionCreateDto> createTransaction(
      @Valid @RequestBody TransactionCreateDto transaction) {
    TransactionCreateDto savedTransaction = transactionService.create(transaction);
    return ResponseEntity.status(HttpStatus.CREATED).body(savedTransaction);
  }

  /**
   * Update transaction by id.
   *
   * @param id
   * @param transactionUpdateDto dto
   * @return
   */
  @PutMapping("/{id}")
  @Operation(
      summary = "Update a specific transaction",
      description = "Role admin require or login with correct user id.")
  @CanAccessTransaction
  public ResponseEntity<TransactionUpdateDto> updateWallet(
      @PathVariable Long id, @Valid @RequestBody TransactionUpdateDto transactionUpdateDto) {
    TransactionUpdateDto updateTransaction = transactionService.update(id, transactionUpdateDto);
    return updateTransaction == null
        ? ResponseEntity.notFound().build()
        : ResponseEntity.ok(updateTransaction);
  }

  /**
   * Request to delete a transaction.
   *
   * @param id transaction id
   * @return No content or not found
   */
  @DeleteMapping("/{id}")
  @Operation()
  @CanAccessTransaction
  public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
    return transactionService.delete(id)
        ? ResponseEntity.noContent().build()
        : ResponseEntity.notFound().build();
  }
}
