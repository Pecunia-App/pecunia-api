package com.pecunia.api.mapper;

import com.pecunia.api.dto.UserDTO;
import com.pecunia.api.model.User;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class UserMapper {

  public UserDTO convertToDTO(User user) {

        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstname(user.getFirstname());
        userDTO.setLastname(user.getLastname());
        userDTO.setEmail(user.getEmail());

        if (user.getProfilePicture() != null && user.getProfilePicture().getPicture() != null) {
          String base64Picture = Base64.getEncoder().encodeToString(user.getProfilePicture().getPicture());
          userDTO.setProfilePicture(base64Picture);
        }

        return userDTO;
    }
}
