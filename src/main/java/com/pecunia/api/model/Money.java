package com.pecunia.api.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Objects;

/** Value Object Money. */
public class Money {
  private long amount;
  private Currency currency;

  public Money(double amount, Currency currency) {
    this.currency = Objects.requireNonNull(currency);
    this.amount = Math.round(amount * centFactor());
  }

  public Money(long amount, Currency currency) {
    this.currency = Objects.requireNonNull(currency);
    this.amount = amount * centFactor();
  }

  public Money() {}

  public Money(BigDecimal multiply, Currency currency, RoundingMode roundingMode) {}

  private static final int[] cents = new int[] {1, 10, 100, 1000};

  private int centFactor() {
    return cents[currency.getDefaultFractionDigits()];
  }

  public BigDecimal amount() {
    return BigDecimal.valueOf(amount, currency.getDefaultFractionDigits());
  }

  public Currency currency() {
    return currency;
  }

  /**
   * l'euro va être utiliser souvent, un helper constructeur va être utile ici.
   *
   * @param amount un montant
   * @return instance de Money qui va construire le montant et le code du pays au format ISO4217
   */
  public static Money euros(double amount) {
    return new Money(amount, Currency.getInstance("EUR"));
  }

  public boolean equals(Object other) {
    return (other instanceof Money) && equals((Money) other);
  }

  public boolean equals(Money other) {
    return currency.equals(other.currency) && (amount == other.amount);
  }

  /** Quand on définit les methodes equals, on doit avoir un hash. */
  @Override
  public int hashCode() {
    return Objects.hash(amount, currency);
  }

  public Money add(Money other) {
    validateSameCurrency(other);
    return newMoney(amount + other.amount);
  }

  public Money substract(Money other) {
    validateSameCurrency(other);
    return newMoney(amount - other.amount);
  }

  public Money multiply(double amount) {
    return multiply(new BigDecimal(amount));
  }

  public Money multiply(BigDecimal amount) {
    return multiply(amount, RoundingMode.HALF_EVEN);
  }

  public Money multiply(BigDecimal amount, RoundingMode roundingMode) {
    return new Money(amount().multiply(amount), currency, roundingMode);
  }

  public Money[] allocate(long[] ratios) {
    long total = 0;
    for (int i = 0; i < ratios.length; i++) {
      total += ratios[i];
    }
    long remainder = amount;
    Money[] results = new Money[ratios.length];
    for (int i = 0; i < results.length; i++) {
      results[i] = newMoney(amount * ratios[i] / total);
      remainder -= results[i].amount;
    }
    for (int i = 0; i < remainder; i++) {
      results[i].amount++;
    }
    return results;
  }

  /**
   * Methode de base pour la comparaison est `compareTo`.
   *
   * @param other Object à comparer
   * @return compareTo class Money
   */
  public int compareTo(Object other) {
    return compareTo((Money) other);
  }

  public int compareTo(Money other) {
    validateSameCurrency(other);
    if (amount < other.amount) {
      return -1;
    } else if (amount == other.amount) {
      return 0;
    }
    return 1;
  }

  public boolean greaterThan(Money other) {
    return (compareTo(other) > 0);
  }

  private Money newMoney(long amount) {
    Money money = new Money();
    money.currency = this.currency;
    money.amount = amount;
    return money;
  }

  private void validateSameCurrency(Money other) {
    if (!this.currency.equals(other.currency)) {
      throw new IllegalArgumentException("Cannot operate on different currencies");
    }
  }
}
