package com.pecunia.api.exception;

public class TransactionTypeNotSupportedException extends RuntimeException {
  /**
   * Transaction Type Not supported Exception.
   *
   * @param message message d'erreur
   */
  public TransactionTypeNotSupportedException(String message) {
    super(message);
  }
}
