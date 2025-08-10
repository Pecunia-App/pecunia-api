package com.pecunia.api.model;

import com.pecunia.api.exception.CannotAddTwoCurrenciesException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Objects;

/** Value Object Money. C'est une implémentaton du Money Pattern de Martin Fowler. */
public class Money implements Comparable<Money> {
  private static final int[] cents = new int[] {1, 10, 100, 1000};

  /**
   * l'euro va être utiliser souvent, un helper constructeur va être utile ici.
   *
   * @param amount un montant
   * @return instance de Money qui va construire le montant et le code du pays au format ISO4217
   */
  public static Money euros(final double amount) {
    return new Money(amount, Currency.getInstance("EUR"));
  }

  private long amount;

  private Currency currency;

  public Money() {}

  public Money(final double amount, final Currency currency) {
    this.currency = Objects.requireNonNull(currency);
    this.amount = Math.round(amount * centFactor());
  }

  public Money(final long amount, final Currency currency) {
    this.currency = Objects.requireNonNull(currency);
    this.amount = amount * centFactor();
  }

  public Money(final BigDecimal amount, final Currency currency, final RoundingMode roundingMode) {
    this.currency = Objects.requireNonNull(currency);
    this.amount =
        amount.multiply(BigDecimal.valueOf(centFactor())).setScale(0, roundingMode).longValue();
  }

  public BigDecimal amount() {
    return BigDecimal.valueOf(amount, currency.getDefaultFractionDigits());
  }

  public Currency currency() {
    return currency;
  }

  /**
   * Retour du code la currency version ISO4217.
   *
   * @return Currency Code ISO4217
   */
  public String getCurrencyCode() {
    return currency().getCurrencyCode();
  }

  /**
   * Retourne le symbole de la devise (€, $, £, ¥, etc.) Utilise la Locale par défaut du système de
   * l'user.
   *
   * @return symbole de la currency
   */
  public String getCurrencySymbol() {
    return currency().getSymbol();
  }

  public boolean equals(final Object other) {
    return (other instanceof Money) && equals((Money) other);
  }

  public boolean equals(final Money other) {
    return currency.equals(other.currency) && (amount == other.amount);
  }

  /** Quand on définit les methodes equals, on doit avoir un hash. */
  @Override
  public int hashCode() {
    return Objects.hash(amount, currency);
  }

  public Money add(final Money other) {
    validateSameCurrency(other);
    return newMoney(amount + other.amount);
  }

  public Money substract(final Money other) {
    validateSameCurrency(other);
    return newMoney(amount - other.amount);
  }

  public Money multiply(final double amount) {
    return multiply(new BigDecimal(amount));
  }

  public Money multiply(final BigDecimal amount) {
    return multiply(amount, RoundingMode.HALF_EVEN);
  }

  public Money multiply(final BigDecimal amount, final RoundingMode roundingMode) {
    return new Money(amount().multiply(amount), currency, roundingMode);
  }

  /**
   * Permet de répartir un montant proportionnel sans perte de centimes.
   *
   * @param ratios proportions
   * @return la répartition sans perte de centimes
   */
  public Money[] allocate(final long[] ratios) {
    long total = 0;
    for (int i = 0; i < ratios.length; i++) {
      total += ratios[i];
    }
    long remainder = amount;
    final Money[] results = new Money[ratios.length];
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
   * Methode de base pour la comparaison est `compareTo`. Avec l'implementation de `Comparable`
   * permet d'eviter un ClassCastException en faisant un cast forcer (eg `((Money) other))`.
   *
   * @param other Object à comparer
   * @return compareTo class Money
   */
  @Override
  public int compareTo(final Money other) {
    validateSameCurrency(other);
    return Long.compare(this.amount, other.amount);
  }

  public boolean greaterThan(final Money other) {
    return (compareTo(other) > 0);
  }

  private int centFactor() {
    return cents[currency.getDefaultFractionDigits()];
  }

  private Money newMoney(final long amount) {
    final Money money = new Money();
    money.currency = this.currency;
    money.amount = amount;
    return money;
  }

  private void validateSameCurrency(final Money other) {
    if (!this.currency.equals(other.currency)) {
      throw new CannotAddTwoCurrenciesException("Cannot operate on different currencies");
    }
  }
}
