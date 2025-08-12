package com.pecunia.api;

import static org.assertj.core.api.Assertions.assertThat;

import com.pecunia.api.model.Money;
import com.pecunia.api.model.MoneyConverter;
import org.junit.jupiter.api.Test;

/** MoneyConverterTest. */
public class MoneyConverterTest {
  @Test
  void testMoneyConverter() {
    MoneyConverter converter = new MoneyConverter();
    Money originalMoney = Money.euros(123.45);

    String dbValue = converter.convertToDatabaseColumn(originalMoney);
    Money reconstructedMoney = converter.convertToEntityAttribute(dbValue);

    assertThat(dbValue).isEqualTo("123.45|EUR");
    assertThat(reconstructedMoney.amount()).isEqualByComparingTo(originalMoney.amount());
    assertThat(reconstructedMoney.getCurrencyCode()).isEqualTo("EUR");
  }
}
