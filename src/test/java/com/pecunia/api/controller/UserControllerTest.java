package com.pecunia.api.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pecunia.api.dto.UserDTO;
import com.pecunia.api.exception.ResourceNotFoundException;
import com.pecunia.api.repository.UserRepository;
import com.pecunia.api.security.JwtAuthenticationFilter;
import com.pecunia.api.security.JwtService;
import com.pecunia.api.service.CustomUserDetailsService;
import com.pecunia.api.service.UserService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
    controllers = UserController.class,
    excludeFilters = {
      @ComponentScan.Filter(
          type = FilterType.ASSIGNABLE_TYPE,
          classes = JwtAuthenticationFilter.class)
    })
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private UserService userService;

  @MockitoBean private JwtService jwtService;

  @MockitoBean private CustomUserDetailsService customUserDetailsService;

  @Mock private UserRepository userRepository;

  @Test
  void testGetAllUsers() throws Exception {

    UserDTO userDTO1 = new UserDTO();
    userDTO1.setFirstname("Peter");
    userDTO1.setLastname("Brown");
    userDTO1.setEmail("peter@pecunia.com");

    UserDTO userDTO2 = new UserDTO();
    userDTO2.setFirstname("Franz");
    userDTO2.setLastname("Ferdinan");
    userDTO2.setEmail("franz@pecunia.com");

    when(userService.getAllUsers()).thenReturn(List.of(userDTO1, userDTO2));

    mockMvc
        .perform(get("/users"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].firstname").value("Peter"))
        .andExpect(jsonPath("$[0].lastname").value("Brown"));
  }

  @Test
  void testGetUserById_UserExists() throws Exception {

    UserDTO userDTO1 = new UserDTO();
    userDTO1.setFirstname("Peter");
    userDTO1.setLastname("Brown");
    userDTO1.setEmail("peter@pecunia.com");

    when(userService.getUserById(1L)).thenReturn(userDTO1);

    mockMvc
        .perform(get("/users/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.email").value("peter@pecunia.com"));
  }

  @Test
  void testGetUserById_UserNotFound() throws Exception {
    when(userService.getUserById(99L)).thenThrow(new ResourceNotFoundException("User not found"));
    mockMvc.perform(get("/users/99")).andExpect(status().isNotFound());
  }
}
