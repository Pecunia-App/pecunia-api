package com.pecunia.api.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.math.BigDecimal;
import java.util.Currency;

/**
 * MoneyConverter pour transformer le Value Object Money en data JPA. Format de stockage : -
 * "123.45|EUR"
 */
@Converter(autoApply = true)
public class MoneyConverter implements AttributeConverter<Money, String> {
  @Override
  public String convertToDatabaseColumn(Money money) {
    if (money == null) {
      return null;
    }
    return money.amount() + "|" + money.getCurrencyCode();
  }

  @Override
  public Money convertToEntityAttribute(String dbData) {
    if (dbData == null || dbData.isEmpty()) {
      return null;
    }
    String[] parts = dbData.split("\\|");
    if (parts.length != 2) {
      throw new IllegalArgumentException("Invalid Foramt for Money data" + dbData);
    }
    BigDecimal amount = new BigDecimal(parts[0]);
    Currency currency = Currency.getInstance(parts[1]);
    return new Money(amount.doubleValue(), currency);
  }
}
