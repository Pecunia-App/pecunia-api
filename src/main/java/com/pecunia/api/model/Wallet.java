package com.pecunia.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.beans.Transient;
import java.math.BigDecimal;
import javax.money.MonetaryAmount;
import org.javamoney.moneta.Money;

/** Wallet. */
@Entity
@Table(name = "wallet")
public class Wallet {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @Column(name = "amount_balance")
  private BigDecimal amount;

  /** ISO 4217 currency code (e.g., "EUR", "USD") for the wallet's balance. */
  private String currency;

  @OneToOne(mappedBy = "wallet", optional = false)
  private User user;

  /**
   * {@code Transient} est utile pour ne pas directement mappé à une colonne d'une table en BDD. Il
   * est construit dynamiquement à partir des champs persistés (ex: amount et currency)
   *
   * @return object {@link MonetaryAmount} = {@code amount} + {@code currency} ou {@code null} si
   *     incomplet.
   */
  @Transient
  public MonetaryAmount getAmountBalance() {
    return (amount != null || currency != null) ? Money.of(amount, currency) : null;
  }

  /**
   * Setter de getAmountBalance utilisant toujours une variable fabriqué à partir des champs
   * persistés.
   *
   * @param monetaryAmount amount to be define
   */
  @Transient
  public void setAmountBalance(MonetaryAmount monetaryAmount) {
    if (monetaryAmount != null) {
      this.amount = monetaryAmount.getNumber().numberValueExact(BigDecimal.class);
      this.currency = monetaryAmount.getCurrency().getCurrencyCode();
    } else {
      this.amount = null;
      this.currency = null;
    }
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }
}
