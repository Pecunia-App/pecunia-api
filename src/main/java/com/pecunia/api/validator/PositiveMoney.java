package com.pecunia.api.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = MoneyPositiveOrZeroValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PositiveMoney {
  String message() default "Le montant ne peut pas être négatif.";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
