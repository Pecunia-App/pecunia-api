package com.pecunia.api.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pecunia.api.dto.ProfilePictureDTO;
import com.pecunia.api.security.JwtAuthenticationFilter;
import com.pecunia.api.security.JwtService;
import com.pecunia.api.service.CustomUserDetailsService;
import com.pecunia.api.service.ProfilePictureService;
import java.util.Base64;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
    controllers = ProfilePictureController.class,
    excludeFilters = {
      @ComponentScan.Filter(
          type = FilterType.ASSIGNABLE_TYPE,
          classes = JwtAuthenticationFilter.class)
    })
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class ProfilePictureControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private ProfilePictureService profilePictureService;

  @MockitoBean private JwtService jwtService;

  @MockitoBean private CustomUserDetailsService customUserDetailsService;

  @Test
  void testGetProfilePictureByUserId() throws Exception {
    long userId = 1L;
    byte[] bytes = new byte[] {1, 2, 3};
    String expectedBase64 = Base64.getEncoder().encodeToString(bytes);

    ProfilePictureDTO dto = new ProfilePictureDTO();
    dto.setPicture(expectedBase64);
    dto.setUserId(userId);

    when(profilePictureService.getProfilePicture(userId)).thenReturn(dto);

    mockMvc
        .perform(get("/profile-pictures/users/{userId}", userId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.picture").value(expectedBase64))
        .andExpect(jsonPath("$.userId").value((int) userId));
  }
}
