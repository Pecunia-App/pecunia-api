package com.pecunia.api.validator;

import com.pecunia.api.model.Money;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/** Document. */
public class MoneyPositiveOrZeroValidator implements ConstraintValidator<PositiveMoney, Money> {
  @Override
  public boolean isValid(Money value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }
    return value.amount().signum() >= 0;
  }
}
