package com.pecunia.api.security;

import com.pecunia.api.dto.UserRegistrationDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatcherValidator implements ConstraintValidator<PasswordMatches, Object> {

  @Override
  public void initialize(PasswordMatches constraintAnnotation) {
  }

  @Override
  public boolean isValid(Object obj, ConstraintValidatorContext context) {
    if (obj instanceof UserRegistrationDTO user) {

      String password = user.getPassword();
      String confirmPassword = user.getConfirmPassword();

      boolean valid = password != null && password.equals(confirmPassword);

      if (!valid) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("Les mots de passe ne correspondent pas.")
          .addPropertyNode("confirmPassword")
          .addConstraintViolation();
      }
      return valid;
    }

    return false;
  }
}
