package com.pecunia.api.mapper;

import com.pecunia.api.dto.ProfilePictureDTO;
import com.pecunia.api.model.ProfilePicture;
import java.util.Base64;
import org.springframework.stereotype.Component;

@Component
public class ProfilePictureMapper {
  public ProfilePicture convertToEntity(ProfilePictureDTO profilePictureDTO) {
    ProfilePicture profilePicture = new ProfilePicture();

    if (profilePictureDTO.getPicture() != null) {
      profilePicture.setPicture(Base64.getDecoder().decode(profilePictureDTO.getPicture()));
    }
    return profilePicture;
  }

  public ProfilePictureDTO convertToDTO(ProfilePicture profilePicture) {
    ProfilePictureDTO profilePictureDTO = new ProfilePictureDTO();
    profilePictureDTO.setId(profilePicture.getId());

    if (profilePicture.getPicture() != null) {
      // encoder en Base64 pour exposer côté API
      profilePictureDTO.setPicture(Base64.getEncoder().encodeToString(profilePicture.getPicture()));
    }
    if (profilePicture.getUser() != null) {
      profilePictureDTO.setUserId(profilePicture.getUser().getId());
    }
    return profilePictureDTO;
  }
}
