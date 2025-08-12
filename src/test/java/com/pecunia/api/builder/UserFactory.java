package com.pecunia.api.builder;

import com.pecunia.api.model.User;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

public class UserFactory {
  public static UserBuilder user() {
    return new UserBuilder();
  }

  public static class UserBuilder {
    private User user = new User();

    public UserBuilder withName(String firstname, String lastname) {
      user.setFirstname(firstname);
      user.setLastname(lastname);
      return this;
    }

    public UserBuilder withEmail(String email) {
      user.setEmail(email);
      return this;
    }

    public User build(TestEntityManager entityManager) {
      if (user.getPassword() == null) {
        user.setPassword("DefaultPassword123!!");
      }
      return entityManager.persistAndFlush(user);
    }
  }
}
