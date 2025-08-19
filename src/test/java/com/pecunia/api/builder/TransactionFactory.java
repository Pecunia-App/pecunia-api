package com.pecunia.api.builder;

import com.pecunia.api.model.Money;
import com.pecunia.api.model.Transaction;
import com.pecunia.api.model.TransactionType;
import com.pecunia.api.model.Wallet;
import java.util.HashSet;
import java.util.Set;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

public class TransactionFactory {
  public static TransactionBuilder transaction() {
    return new TransactionBuilder();
  }

  public static class TransactionBuilder {
    private Transaction transaction = new Transaction();
    private Wallet wallet;

    public TransactionBuilder withType(TransactionType type) {
      transaction.setType(type);
      return this;
    }

    public TransactionBuilder withAmount(Money amount) {
      transaction.setAmount(amount);
      return this;
    }

    public TransactionBuilder withNote(String note) {
      transaction.setNote(note);
      return this;
    }

    public TransactionBuilder forWallet(Wallet wallet) {
      this.wallet = wallet;
      return this;
    }

    public Transaction build(TestEntityManager entityManager) {
      if (wallet == null) {
        throw new IllegalStateException(
            "Transaction must have a wallet. Use 'forWallet()' static method");
      }
      transaction.setWallet(wallet);
      Set<Transaction> transactions = wallet.getTransactions();
      if (transactions == null) {
        transactions = new HashSet<>();
        wallet.setTransactions(transactions);
      }
      return entityManager.persistAndFlush(transaction);
    }
  }
}
