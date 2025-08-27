package com.pecunia.api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pecunia.api.exception.CannotAddTwoCurrenciesException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Objects;

/** Value Object Money. C'est une implémentaton du Money Pattern de Martin Fowler */
public class Money implements Comparable<Money> {
  // Tableau pour gérer les facteurs de conversion pour chaque devise (par exemple, 100 pour l'euro,
  // 100 pour le dollar)
  private static final int[] cents = new int[] {1, 10, 100, 1000};

  /**
   * Crée une instance de <code>Money</code> représentant un montant en euros (EUR).
   *
   * @param amount le montant en euros
   * @return une nouvelle instance de <code>Money</code> avec la devise EUR
   */
  public static Money euros(final double amount) {
    return new Money(amount, Currency.getInstance("EUR"));
  }

  /**
   * Crée une instance de <code>Money</code> représentant un montant en dollars américains (USD).
   *
   * @param amount le montant en dollars
   * @return une nouvelle instance de <code>Money</code> avec la devise USD
   */
  public static Money dollars(final double amount) {
    return new Money(amount, Currency.getInstance("USD"));
  }

  private long amount;

  private Currency currency;

  /** Constructeur par défaut. Nécessaire pour Jackson pour la sérialisation JSON. */
  public Money() {}

  /**
   * Crée une instance de <code>Money</code> à partir d'un montant en double et d'une devise.
   *
   * @param amount le montant en double
   * @param currency la devise associée au montant
   */
  public Money(final double amount, final Currency currency) {
    this.currency = Objects.requireNonNull(currency);
    this.amount = Math.round(amount * centFactor());
  }

  /**
   * Constructeur pour la création d'une instance de <code>Money</code> à partir d'un montant en
   * double et du code de la devise en format ISO 4217.
   *
   * @param amount le montant en double
   * @param currencyCode le code ISO 4217 de la devise
   */
  @JsonCreator
  public Money(
      @JsonProperty("amount") double amount, @JsonProperty("currency") String currencyCode) {
    this.currency = Currency.getInstance(currencyCode);
    this.amount = Math.round(amount * centFactor());
  }

  /**
   * Crée une instance de <code>Money</code> à partir d'un montant en long (en centimes) et d'une
   * devise.
   *
   * @param amount le montant en centimes
   * @param currency la devise associée
   */
  public Money(final long amount, final Currency currency) {
    this.currency = Objects.requireNonNull(currency);
    this.amount = amount * centFactor();
  }

  /**
   * Crée une instance de <code>Money</code> à partir d'un montant en BigDecimal avec un mode
   * d'arrondi.
   *
   * @param amount le montant en BigDecimal
   * @param currency la devise associée
   * @param roundingMode le mode d'arrondi
   */
  public Money(final BigDecimal amount, final Currency currency, final RoundingMode roundingMode) {
    this.currency = Objects.requireNonNull(currency);
    this.amount =
        amount.multiply(BigDecimal.valueOf(centFactor())).setScale(0, roundingMode).longValue();
  }

  /**
   * Retourne le montant en tant que <code>BigDecimal</code> avec la fraction de la devise.
   *
   * @return le montant sous forme de BigDecimal
   */
  @JsonProperty("amount")
  public BigDecimal amount() {
    return BigDecimal.valueOf(amount, currency.getDefaultFractionDigits());
  }

  /**
   * Retourne la devise utilisée pour ce montant.
   *
   * @return la devise associée au montant
   */
  @JsonProperty("currency")
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

  /**
   * Vérifie si cet objet <code>Money</code> est égal à un autre objet.
   *
   * @param other l'objet à comparer
   * @return true si les montants et devises sont égaux, false sinon
   */
  @Override
  public boolean equals(final Object other) {
    return (other instanceof Money) && equals((Money) other);
  }

  /**
   * Vérifie si deux instances de <code>Money</code> sont égales (même montant et devise).
   *
   * @param other l'instance à comparer
   * @return true si les montants et devises sont égaux, false sinon
   */
  public boolean equals(final Money other) {
    return currency.equals(other.currency) && (amount == other.amount);
  }

  /**
   * Retourne le code de hachage de l'objet <code>Money</code>.
   *
   * @return le code de hachage de l'objet
   */
  @Override
  public int hashCode() {
    return Objects.hash(amount, currency);
  }

  /**
   * Ajoute deux objets <code>Money</code> de même devise et retourne un nouveau <code>Money</code>.
   *
   * @param other le <code>Money</code> à ajouter
   * @return le résultat de l'addition
   * @throws CannotAddTwoCurrenciesException si les devises sont différentes
   */
  public Money add(final Money other) {
    validateSameCurrency(other);
    return newMoney(amount + other.amount);
  }

  /**
   * Soustrait un objet <code>Money</code> de même devise et retourne un nouveau <code>Money</code>.
   *
   * @param other le <code>Money</code> à soustraire
   * @return le résultat de la soustraction
   * @throws CannotAddTwoCurrenciesException si les devises sont différentes
   */
  public Money substract(final Money other) {
    validateSameCurrency(other);
    return newMoney(amount - other.amount);
  }

  /**
   * Multiplie le montant d'argent par un facteur en double et retourne un nouveau <code>Money
   * </code>.
   *
   * @param amount le facteur de multiplication
   * @return un nouveau <code>Money</code> après multiplication
   */
  public Money multiply(final double amount) {
    return multiply(new BigDecimal(amount));
  }

  /**
   * Multiplie le montant d'argent par un facteur en BigDecimal et retourne un nouveau <code>Money
   * </code>.
   *
   * @param amount le facteur de multiplication
   * @return un nouveau <code>Money</code> après multiplication
   */
  public Money multiply(final BigDecimal amount) {
    return multiply(amount, RoundingMode.HALF_EVEN);
  }

  /**
   * Multiplie le montant d'argent par un facteur en BigDecimal avec un mode d'arrondi spécifié.
   * Cette méthode permet de gérer la multiplication avec des montants en décimales et un mode
   * d'arrondi afin d'éviter les erreurs d'approximation dans les calculs financiers.
   *
   * @param amount le facteur de multiplication
   * @param roundingMode le mode d'arrondi à utiliser (par exemple, HALF_EVEN, HALF_UP)
   * @return un nouveau <code>Money</code> après multiplication avec l'arrondi appliqué
   */
  public Money multiply(final BigDecimal amount, final RoundingMode roundingMode) {
    return new Money(amount().multiply(amount), currency, roundingMode);
  }

  /**
   * Permet de répartir un montant proportionnel sans perte de centimes.
   *
   * @param ratios un tableau contenant les ratios de répartition (par exemple, {2, 3, 5})
   * @return un tableau de <code>Money</code> représentant la répartition des montants
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
   * Compare cet objet <code>Money</code> avec un autre objet <code>Money</code>. Methode de base
   * pour la comparaison est `compareTo`. Avec l'implementation de `Comparable` permet d'eviter un
   * ClassCastException en faisant un cast forcer (eg `((Money) other))`.
   *
   * @param other Object à comparer
   * @return compareTo class Money
   */
  @Override
  public int compareTo(final Money other) {
    validateSameCurrency(other);
    return Long.compare(this.amount, other.amount);
  }

  /**
   * Vérifie si cet objet <code>Money</code> est strictement supérieur à un autre objet <code>Money
   * </code>. Cette méthode est utile pour effectuer des comparaisons simples entre montants.
   *
   * @param other l'objet <code>Money</code> à comparer
   * @return true si cet objet est supérieur à l'objet <code>other</code>, sinon false
   */
  public boolean greaterThan(final Money other) {
    return (compareTo(other) > 0);
  }

  @Override
  public String toString() {
    return amount() + "|" + currency;
  }

  /**
   * Retourne le facteur de conversion pour la devise, c'est-à-dire le nombre de centimes que
   * représente une unité de la devise. Par exemple, pour l'euro et le dollar, le facteur est 100.
   *
   * @return le facteur de conversion (généralement 100 ou 1000 selon la devise)
   */
  private int centFactor() {
    return cents[currency.getDefaultFractionDigits()];
  }

  /**
   * Crée un nouvel objet <code>Money</code> avec un montant donné et la même devise que l'objet
   * actuel. Cette méthode est utilisée pour créer des instances de <code>Money</code> avec des
   * montants modifiés sans changer la devise.
   *
   * @param amount le montant à attribuer
   * @return une nouvelle instance de <code>Money</code> avec le même type de devise
   */
  private Money newMoney(final long amount) {
    final Money money = new Money();
    money.currency = this.currency;
    money.amount = amount;
    return money;
  }

  /**
   * Valide que deux objets <code>Money</code> utilisent la même devise. Cette méthode est utilisée
   * avant de réaliser des opérations sur des montants d'argent (comme additionner ou soustraire)
   * pour s'assurer qu'ils sont compatibles.
   *
   * @param other l'objet <code>Money</code> avec lequel l'opération est réalisée
   * @throws CannotAddTwoCurrenciesException si les devises sont différentes
   */
  private void validateSameCurrency(final Money other) {
    if (!this.currency.equals(other.currency)) {
      throw new CannotAddTwoCurrenciesException("Cannot operate on different currencies");
    }
  }
}
