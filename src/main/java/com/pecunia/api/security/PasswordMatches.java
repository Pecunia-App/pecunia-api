package com.pecunia.api.security;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordMatcherValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordMatches {
  String message() default "Les mots de passe ne correspondent pas.";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
