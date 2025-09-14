package com.pecunia.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.pecunia.api.exception.CannotAddTwoCurrenciesException;
import com.pecunia.api.model.Money;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import org.junit.jupiter.api.Test;

/** MoneyTest. */
public class MoneyTest {

  @Test
  public void testConstructorWithDoubleAndCurrency() {
    Money money = new Money(10.50, Currency.getInstance("EUR"));
    assertEquals(new BigDecimal("10.50"), money.amount());
    assertEquals(Currency.getInstance("EUR"), money.currency());
  }

  @Test
  public void testConstructorWithDoubleAndCodeCurrency() {
    Money money = new Money(25.75, "USD");
    assertEquals(new BigDecimal("25.75"), money.amount());
    assertEquals(Currency.getInstance("USD"), money.currency());
  }

  @Test
  public void testConstructorWithLongAndCurrency() {
    Money money = new Money(1050L, Currency.getInstance("EUR"));
    assertEquals(new BigDecimal("1050.00"), money.amount());
    assertEquals(Currency.getInstance("EUR"), money.currency());
  }

  @Test
  public void testConstructorWithBigDecimalAndRoundingMode() {
    BigDecimal amount = new BigDecimal("10.555");
    Money money = new Money(amount, Currency.getInstance("EUR"), RoundingMode.HALF_UP);
    assertEquals(new BigDecimal("10.56"), money.amount());
    assertEquals(Currency.getInstance("EUR"), money.currency());
  }

  @Test
  public void testAllocate() {
    long[] allocation = {3, 7};
    Money[] result = Money.euros(0.05).allocate(allocation);
    assertEquals(Money.euros(0.02), result[0]);
    assertEquals(Money.euros(0.03), result[1]);
  }

  @Test
  public void testEuros() {
    Money euros = Money.euros(15.99);
    assertEquals(new BigDecimal("15.99"), euros.amount());
    assertEquals(Currency.getInstance("EUR"), euros.currency());
    assertEquals("EUR", euros.getCurrencyCode());
  }

  @Test
  public void testDollars() {
    Money dollars = Money.dollars(42.33);
    assertEquals(new BigDecimal("42.33"), dollars.amount());
    assertEquals(Currency.getInstance("USD"), dollars.currency());
    assertEquals("USD", dollars.getCurrencyCode());
  }

  @Test
  public void testGetCurrencySymbol() {
    Money euros = Money.euros(100.0);
    Money dollars = Money.dollars(100.0);

    assertNotNull(euros.getCurrencySymbol());
    assertNotNull(dollars.getCurrencySymbol());
  }

  @Test
  public void testEqualsWithSameMoney() {
    Money money1 = Money.euros(25.50);
    Money money2 = Money.euros(25.50);

    assertTrue(money1.equals(money2));
    assertEquals(money1, money2);
    assertEquals(money1.hashCode(), money2.hashCode());
  }

  @Test
  public void testEqualsWithDifferentAmounts() {
    Money money1 = Money.euros(25.50);
    Money money2 = Money.euros(30.00);

    assertFalse(money1.equals(money2));
    assertNotEquals(money1, money2);
  }

  @Test
  public void testEqualsWithDifferentCurrencies() {
    Money euros = Money.euros(25.50);
    Money dollars = Money.dollars(25.50);

    assertFalse(euros.equals(dollars));
    assertNotEquals(euros, dollars);
  }

  @Test
  public void testAddWithSameCurrency() {
    Money money1 = Money.euros(10.50);
    Money money2 = Money.euros(5.25);
    Money result = money1.add(money2);

    assertEquals(Money.euros(15.75), result);
    assertEquals("EUR", result.getCurrencyCode());
  }

  @Test
  public void testAddWithDifferentCurrencies() {
    Money euros = Money.euros(10.50);
    Money dollars = Money.dollars(5.25);

    assertThrows(
        CannotAddTwoCurrenciesException.class,
        () -> {
          euros.add(dollars);
        });
  }

  @Test
  public void testSubtractWithSameCurrency() {
    Money money1 = Money.euros(15.75);
    Money money2 = Money.euros(5.25);
    Money result = money1.subtract(money2);

    assertEquals(Money.euros(10.50), result);
    assertEquals("EUR", result.getCurrencyCode());
  }

  @Test
  public void testSubtractWithDifferentCurrencies() {
    Money euros = Money.euros(15.75);
    Money dollars = Money.dollars(5.25);

    assertThrows(
        CannotAddTwoCurrenciesException.class,
        () -> {
          euros.subtract(dollars);
        });
  }

  @Test
  public void testMultiplyByDouble() {
    Money money = Money.euros(10.00);
    Money result = money.multiply(2.5);

    assertEquals(Money.euros(25.00), result);
    assertEquals("EUR", result.getCurrencyCode());
  }

  @Test
  public void testMultiplyByBigDecimal() {
    Money money = Money.euros(10.00);
    BigDecimal multiplier = new BigDecimal("1.5");
    Money result = money.multiply(multiplier);

    assertEquals(Money.euros(15.00), result);
    assertEquals("EUR", result.getCurrencyCode());
  }

  @Test
  public void testMultiplyByBigDecimalWithRounding() {
    Money money = Money.euros(10.00);
    BigDecimal multiplier = new BigDecimal("0.333");
    Money result = money.multiply(multiplier, RoundingMode.HALF_EVEN);

    assertEquals(Money.euros(3.33), result);
    assertEquals("EUR", result.getCurrencyCode());
  }

  @Test
  public void testCompareToEqual() {
    Money money1 = Money.euros(25.50);
    Money money2 = Money.euros(25.50);

    assertEquals(0, money1.compareTo(money2));
  }

  @Test
  public void testCompareToGreater() {
    Money money1 = Money.euros(30.00);
    Money money2 = Money.euros(25.50);

    assertTrue(money1.compareTo(money2) > 0);
  }

  @Test
  public void testCompareToSmaller() {
    Money money1 = Money.euros(20.00);
    Money money2 = Money.euros(25.50);

    assertTrue(money1.compareTo(money2) < 0);
  }

  @Test
  public void testCompareToWithDifferentCurrencies() {
    Money euros = Money.euros(25.50);
    Money dollars = Money.dollars(25.50);

    assertThrows(
        CannotAddTwoCurrenciesException.class,
        () -> {
          euros.compareTo(dollars);
        });
  }

  @Test
  public void testGreaterThanTrue() {
    Money money1 = Money.euros(30.00);
    Money money2 = Money.euros(25.50);

    assertTrue(money1.greaterThan(money2));
  }

  @Test
  public void testGreaterThanFalse() {
    Money money1 = Money.euros(20.00);
    Money money2 = Money.euros(25.50);

    assertFalse(money1.greaterThan(money2));
  }

  @Test
  public void testGreaterThanEqual() {
    Money money1 = Money.euros(25.50);
    Money money2 = Money.euros(25.50);

    assertFalse(money1.greaterThan(money2));
  }

  @Test
  public void testToString() {
    Money money = Money.euros(25.50);
    String result = money.toString();

    assertTrue(result.contains("25.50"));
    assertTrue(result.contains("EUR"));
    assertTrue(result.contains("|"));
  }

  @Test
  public void testZeroAmount() {
    Money money = Money.euros(0.00);
    assertEquals(new BigDecimal("0.00"), money.amount());
    assertEquals("EUR", money.getCurrencyCode());
  }

  @Test
  public void testNegativeAmount() {
    Money money = Money.euros(-10.50);
    assertEquals(new BigDecimal("-10.50"), money.amount());
    assertEquals("EUR", money.getCurrencyCode());
  }

  @Test
  public void testVerySmallAmount() {
    Money money = Money.euros(0.01);
    assertEquals(new BigDecimal("0.01"), money.amount());
    assertEquals("EUR", money.getCurrencyCode());
  }

  @Test
  public void testConstructorWithNullCurrency() {
    assertThrows(
        NullPointerException.class,
        () -> {
          new Money(10.0, (Currency) null);
        });
  }

  @Test
  public void testConstructorWithInvalidCurrencyCode() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          new Money(10.0, "INVALID");
        });
  }

  @Test
  public void testPrecisionWithManyDecimals() {
    Money money = new Money(10.999999, Currency.getInstance("EUR"));
    // Le montant devrait être arrondi selon les règles de la devise
    assertNotNull(money.amount());
    assertEquals("EUR", money.getCurrencyCode());
  }
}
