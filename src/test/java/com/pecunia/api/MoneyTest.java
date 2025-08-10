package com.pecunia.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.pecunia.api.model.Money;
import org.junit.jupiter.api.Test;

/** MoneyTest. */
public class MoneyTest {

  @Test
  public void testAllocate() {
    long[] allocation = {3, 7};
    Money[] result = Money.euros(0.05).allocate(allocation);
    assertEquals(Money.euros(0.02), result[0]);
    assertEquals(Money.euros(0.03), result[1]);
  }
}
