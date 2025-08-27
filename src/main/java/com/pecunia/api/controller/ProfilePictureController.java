package com.pecunia.api.controller;

import com.pecunia.api.dto.ProfilePictureDTO;
import com.pecunia.api.exception.DuplicateProfilePictureException;
import com.pecunia.api.service.ProfilePictureService;
import java.io.IOException;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/profile-pictures/users/{userId}")
public class ProfilePictureController {

  private final ProfilePictureService profilePictureService;

  public ProfilePictureController(ProfilePictureService profilePictureService) {
    this.profilePictureService = profilePictureService;
  }

  @PreAuthorize("#userId == authentication.principal.id or hasRole('ROLE_ADMIN')")
  @GetMapping
  public ResponseEntity<ProfilePictureDTO> getProfilePicture(@PathVariable Long userId) {
    ProfilePictureDTO picture = profilePictureService.getProfilePicture(userId);
    return ResponseEntity.ok(picture);
  }

  @PreAuthorize("#userId == authentication.principal.id")
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> uploadProfilePicture(
      @PathVariable Long userId, @RequestParam("file") MultipartFile file) {
    try {
      ProfilePictureDTO savedPicture = profilePictureService.saveProfilePicture(userId, file);
      return ResponseEntity.status(HttpStatus.CREATED).body(savedPicture);
    } catch (DuplicateProfilePictureException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));
    } catch (RuntimeException | IOException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
    }
  }

  @PreAuthorize("#userId == authentication.principal.id")
  @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> updateProfilePicture(
      @PathVariable Long userId, @RequestParam("file") MultipartFile file) {
    try {
      ProfilePictureDTO updatedPicture = profilePictureService.updateProfilePicture(userId, file);
      return ResponseEntity.ok(updatedPicture);
    } catch (DuplicateProfilePictureException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));
    } catch (RuntimeException | IOException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
    }
  }

  @PreAuthorize("#userId == authentication.principal.id")
  @DeleteMapping
  public ResponseEntity<Void> deleteProfilePicture(@PathVariable Long userId) {
    boolean deleted = profilePictureService.deleteProfilePicture(userId);
    if (deleted) {
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }
}
