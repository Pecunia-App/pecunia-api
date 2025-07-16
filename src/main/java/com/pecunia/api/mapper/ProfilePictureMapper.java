package com.pecunia.api.mapper;

import com.pecunia.api.dto.ProfilePictureDTO;
import com.pecunia.api.model.ProfilePicture;
import org.springframework.stereotype.Component;


@Component
public class ProfilePictureMapper {
  public ProfilePicture convertToEntity(ProfilePictureDTO profilePictureDTO) {
    ProfilePicture profilePicture = new ProfilePicture();
    profilePicture.setPicture(profilePictureDTO.getPicture());

    return profilePicture;
  }

  public ProfilePictureDTO convertToDTO(ProfilePicture profilePicture) {
    ProfilePictureDTO profilePictureDTO = new ProfilePictureDTO();
    profilePictureDTO.setId(profilePicture.getId());
    profilePictureDTO.setPicture(profilePicture.getPicture());
    if (profilePicture.getUser() != null) {
      profilePictureDTO.setUserId(profilePicture.getUser().getId());
    }
    return profilePictureDTO;
  }
}
  

