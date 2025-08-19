package com.pecunia.api.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;

/** Wallet. */
@Entity
@Table(name = "wallet")
public class Wallet extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @Convert(converter = MoneyConverter.class)
  @Column(name = "amount_balance")
  private Money amountBalance;

  @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private Set<Transaction> transactions = new HashSet<>();

  public Set<Transaction> getTransactions() {
    return transactions;
  }

  public void setTransactions(Set<Transaction> transactions) {
    this.transactions = transactions;
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

  public Money getAmountBalance() {
    return amountBalance;
  }

  public void setAmountBalance(Money amountBalance) {
    this.amountBalance = amountBalance;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
}
