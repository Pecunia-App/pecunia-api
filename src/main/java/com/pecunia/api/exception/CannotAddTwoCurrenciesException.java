package com.pecunia.api.exception;

/** CannotAddTwoCurrenciesException. */
public class CannotAddTwoCurrenciesException extends RuntimeException {
  /**
   * Cannot add 2 currencies.
   *
   * @param message message d'erreur
   */
  public CannotAddTwoCurrenciesException(String message) {
    super(message);
  }
}
