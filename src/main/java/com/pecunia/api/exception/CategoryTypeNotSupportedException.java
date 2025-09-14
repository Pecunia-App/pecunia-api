package com.pecunia.api.exception;

public class CategoryTypeNotSupportedException extends RuntimeException {
  /**
   * Transaction Type Not supported Exception.
   *
   * @param message message d'erreur
   */
  public CategoryTypeNotSupportedException(String message) {
    super(message);
  }
}
