package com.pecunia.api.builder;

import com.pecunia.api.model.Money;
import com.pecunia.api.model.User;
import com.pecunia.api.model.Wallet;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

public class WalletFactory {
  public static WalletBuilder wallet() {
    return new WalletBuilder();
  }

  public static class WalletBuilder {
    private Wallet wallet = new Wallet();
    private User user;

    public WalletBuilder withName(String name) {
      wallet.setName(name);
      return this;
    }

    public WalletBuilder withAmount(Money amount) {
      wallet.setAmountBalance(amount);
      return this;
    }

    public WalletBuilder forUser(User user) {
      this.user = user;
      return this;
    }

    public Wallet build(TestEntityManager entityManager) {
      if (user == null) {
        throw new IllegalStateException("Wallet must have an user. Use 'forUser() static method.");
      }
      wallet.setUser(user);
      user.setWallet(wallet);
      return entityManager.persistAndFlush(wallet);
    }
  }
}
