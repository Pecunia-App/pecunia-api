package com.pecunia.api.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.pecunia.api.model.User;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class UserRepositoryTest {

  @Autowired private UserRepository userRepository;

  @Test
  void testFindAllUsers() {
    User user1 = new User();
    user1.setFirstname("Peter");
    user1.setLastname("Brown");
    user1.setEmail("peter@pecunia.com");
    user1.setPassword("password");

    User user2 = new User();
    user2.setFirstname("Franz");
    user2.setLastname("Ferdinan");
    user2.setEmail("franz@pecunia.com");
    user2.setPassword("password");

    userRepository.saveAll(List.of(user1, user2));

    List<User> users = userRepository.findAll();

    assertThat(users).hasSize(2);

    assertThat(users.get(0).getFirstname()).isEqualTo(user1.getFirstname());
    assertThat(users.get(0).getLastname()).isEqualTo(user1.getLastname());
    assertThat(users.get(0).getEmail()).isEqualTo(user1.getEmail());
    assertThat(users.get(0).getPassword()).isEqualTo(user1.getPassword());

    assertThat(users.get(1).getFirstname()).isEqualTo(user2.getFirstname());
    assertThat(users.get(1).getLastname()).isEqualTo(user2.getLastname());
    assertThat(users.get(1).getEmail()).isEqualTo(user2.getEmail());
    assertThat(users.get(1).getPassword()).isEqualTo(user2.getPassword());
  }
}
