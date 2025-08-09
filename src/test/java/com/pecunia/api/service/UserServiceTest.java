package com.pecunia.api.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.pecunia.api.dto.UserDTO;
import com.pecunia.api.mapper.UserMapper;
import com.pecunia.api.model.User;
import com.pecunia.api.repository.UserRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock private UserRepository userRepository;

  @Mock private UserMapper userMapper;

  @InjectMocks private UserService userService;

  @Test
  void testGetAllUsers() {

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

    when(userRepository.findAll()).thenReturn(List.of(user1, user2));

    UserDTO userDTO1 = new UserDTO();
    userDTO1.setFirstname("Peter");
    userDTO1.setLastname("Brown");
    userDTO1.setEmail("peter@pecunia.com");

    UserDTO userDTO2 = new UserDTO();
    userDTO2.setFirstname("Franz");
    userDTO2.setLastname("Ferdinan");
    userDTO2.setEmail("franz@pecunia.com");

    when(userRepository.findAll()).thenReturn(List.of(user1, user2));
    when(userMapper.convertToDTO(user1)).thenReturn(userDTO1);
    when(userMapper.convertToDTO(user2)).thenReturn(userDTO2);

    List<UserDTO> userDTOs = userService.getAllUsers();

    assertThat(userDTOs).hasSize(2);

    assertThat(userDTOs.get(0).getFirstname()).isEqualTo(user1.getFirstname());
    assertThat(userDTOs.get(0).getLastname()).isEqualTo(user1.getLastname());
    assertThat(userDTOs.get(0).getEmail()).isEqualTo(user1.getEmail());
    assertThat(userDTOs.get(0).getProfilePicture()).isEqualTo(user1.getProfilePicture());

    assertThat(userDTOs.get(1).getFirstname()).isEqualTo(user2.getFirstname());
    assertThat(userDTOs.get(1).getLastname()).isEqualTo(user2.getLastname());
    assertThat(userDTOs.get(1).getEmail()).isEqualTo(user2.getEmail());
    assertThat(userDTOs.get(1).getProfilePicture()).isEqualTo(user2.getProfilePicture());
  }
}
