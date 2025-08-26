package com.pecunia.api.service;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.pecunia.api.model.ProfilePicture;
import com.pecunia.api.model.User;
import com.pecunia.api.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class ProfilePictureServiceIntegrationTest {

  @Autowired private UserRepository userRepository;

  @Test
  void shouldPersistAndLoadUserWithProfilePicture() {
    // given
    ProfilePicture picture = new ProfilePicture();
    picture.setPicture(new byte[] {1, 2, 3});

    User user = new User();
    user.setFirstname("Jane");
    user.setLastname("Doe");
    user.setEmail("user.profilepic.it@test.local");
    user.setPassword("secret");
    user.setProfilePicture(picture);
    // si la relation est bidirectionnelle, assure la cohérence des deux côtés
    picture.setUser(user);

    // when
    User saved = userRepository.save(user);
    assertNotNull(saved.getId());

    // then: on relit depuis le repository (transaction distincte)
    User reloaded = userRepository.findById(saved.getId()).orElseThrow();
    assertNotNull(reloaded.getProfilePicture());
    assertArrayEquals(new byte[] {1, 2, 3}, reloaded.getProfilePicture().getPicture());
  }
}
