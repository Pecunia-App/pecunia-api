package com.pecunia.api.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.pecunia.api.model.ProfilePicture;
import com.pecunia.api.model.User;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class ProfilePictureRepositoryTest {

  @Autowired private ProfilePictureRepository profilePictureRepository;
  @Autowired private UserRepository userRepository;

  @Test
  void testFindAllProfilePictures() {

    ProfilePicture profilePicture1 = new ProfilePicture();
    profilePicture1.setPicture(new byte[] {1, 2, 3});

    ProfilePicture profilePicture2 = new ProfilePicture();
    profilePicture2.setPicture(new byte[] {4, 5, 6});

    profilePictureRepository.saveAll(List.of(profilePicture1, profilePicture2));

    List<ProfilePicture> profilePictures = profilePictureRepository.findAll();

    assertThat(profilePictures).hasSize(2);
    assertThat(profilePictures.get(0).getPicture()).isEqualTo(new byte[] {1, 2, 3});
    assertThat(profilePictures.get(1).getPicture()).isEqualTo(new byte[] {4, 5, 6});
  }

  @Test
  void testUserWithProfilePicture() {
    ProfilePicture profilePicture = new ProfilePicture();
    profilePicture.setPicture(new byte[] {1, 2, 3});

    User user = new User();
    user.setFirstname("Jane");
    user.setLastname("Doe");
    user.setEmail("jane@pecunia.com");
    user.setPassword("password");
    user.setProfilePicture(profilePicture);

    userRepository.save(user);

    User found = userRepository.findById(user.getId()).orElseThrow();

    assertThat(found.getFirstname()).isEqualTo("Jane");
  }
}
