package com.pecunia.api.controller;

import com.pecunia.api.dto.ProfilePictureDTO;
import com.pecunia.api.service.ProfilePictureService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/profile-picture")
public class ProfilePictureController {

  private final ProfilePictureService profilePictureService;

  public ProfilePictureController(ProfilePictureService profilePictureService) {
    this.profilePictureService = profilePictureService;
  }

  @PreAuthorize("#userId == authentication.principal.id or hasRole('ROLE_ADMIN')")
  @GetMapping("/{userId}")
  public ResponseEntity<ProfilePictureDTO> getProfilePicture(@PathVariable Long userId) {
    ProfilePictureDTO picture = profilePictureService.getProfilePicture(userId);
    return ResponseEntity.ok(picture);
  }

  @PreAuthorize("#userId == authentication.principal.id")
  @PostMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ProfilePictureDTO> uploadProfilePicture(
          @PathVariable Long userId,
          @RequestParam("file") MultipartFile file) throws IOException {

    byte[] resizedImage = profilePictureService.resizeAndValidateImage(file);
    ProfilePictureDTO savedPicture = profilePictureService.saveProfilePicture(userId, resizedImage);
    return ResponseEntity.status(HttpStatus.CREATED).body(savedPicture);
  }
}


