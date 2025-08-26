package com.pecunia.api.service;

import com.pecunia.api.dto.ProfilePictureDTO;
import com.pecunia.api.model.ProfilePicture;
import com.pecunia.api.model.User;
import com.pecunia.api.repository.UserRepository;
import org.junit.jupiter.api.Test;
import com.pecunia.api.mapper.ProfilePictureMapper;
import com.pecunia.api.repository.ProfilePictureRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfilePictureServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private ProfilePictureRepository profilePictureRepository;

  @Mock
  private ProfilePictureMapper profilePictureMapper;

  @InjectMocks
  private ProfilePictureService profilePictureService;

  @Test
  void testGetAllProfilePicture() {
    User user = new User();
    user.setId(1L);

    ProfilePicture picture = new ProfilePicture();
    picture.setPicture(new byte[]{1, 2, 3});
    user.setProfilePicture(picture);

    ProfilePictureDTO dto = new ProfilePictureDTO();
    dto.setPicture(new byte[]{1, 2, 3});

    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(profilePictureMapper.convertToDTO(picture)).thenReturn(dto);

    ProfilePictureDTO result = profilePictureService.getProfilePicture(1L);

    assertThat(result.getPicture()).isEqualTo(new byte[]{1, 2, 3});
    verify(userRepository).findById(1L);
  }
}
