package com.pecunia.api.mapper;

import com.pecunia.api.dto.UserDTO;
import com.pecunia.api.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

  public UserDTO convertToDTO(User user) {

    UserDTO userDTO = new UserDTO();
    userDTO.setId(user.getId());
    userDTO.setFirstname(user.getFirstname());
    userDTO.setLastname(user.getLastname());
    userDTO.setProfilePicture(user.getProfilePicture());
    userDTO.setEmail(user.getEmail());

    return userDTO;
  }
}
