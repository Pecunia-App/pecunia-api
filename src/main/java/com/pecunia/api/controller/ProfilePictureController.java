package com.pecunia.api.security;

import com.pecunia.api.dto.ProfilePictureDTO;
import com.pecunia.api.exception.DuplicateProfilePictureException;
import com.pecunia.api.service.ProfilePictureService;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/profile-pictures/users/{userId}")
public class ProfilePictureController {

  private static final Logger logger = LoggerFactory.getLogger(ProfilePictureController.class);
  private final ProfilePictureService profilePictureService;

  public ProfilePictureController(ProfilePictureService profilePictureService) {
    this.profilePictureService = profilePictureService;
  }

  @PreAuthorize("#userId == authentication.principal.id or hasRole('ROLE_ADMIN')")
  @GetMapping
  public ResponseEntity<ProfilePictureDTO> getProfilePicture(@PathVariable Long userId) {
    logger.info("🔍 GET profile picture for userId: {}", userId);
    ProfilePictureDTO picture = profilePictureService.getProfilePicture(userId);
    return ResponseEntity.ok(picture);
  }

  @PreAuthorize("#userId == authentication.principal.id or hasRole('ROLE_ADMIN')")
  @GetMapping(value = "/raw", produces = MediaType.IMAGE_PNG_VALUE)
  public ResponseEntity<byte[]> getProfilePictureRaw(@PathVariable Long userId) {
    logger.info("🖼️ GET raw profile picture for userId: {}", userId);
    byte[] body = profilePictureService.getProfilePictureBytesOrDefault(userId);
    return ResponseEntity.ok()
        .contentType(MediaType.IMAGE_PNG)
        .cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS).cachePublic())
        .body(body);
  }

  @PreAuthorize("#userId == authentication.principal.id")
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> uploadProfilePicture(
      @PathVariable Long userId, @RequestParam("file") MultipartFile file) {
    logger.info(
        "📤 POST profile picture for userId: {}, filename: {}", userId, file.getOriginalFilename());
    try {
      ProfilePictureDTO savedPicture = profilePictureService.saveProfilePicture(userId, file);
      logger.info("✅ Profile picture saved successfully for userId: {}", userId);
      return ResponseEntity.status(HttpStatus.CREATED).body(savedPicture);
    } catch (DuplicateProfilePictureException e) {
      logger.warn("⚠️ Duplicate profile picture for userId: {}", userId);
      return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));
    } catch (RuntimeException | IOException e) {
      logger.error("❌ Error uploading profile picture for userId: {}", userId, e);
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
    }
  }

  @PreAuthorize("#userId == authentication.principal.id")
  @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> updateProfilePicture(
      @PathVariable Long userId, @RequestParam("file") MultipartFile file) {
    logger.info(
        "🔄 PUT profile picture for userId: {}, filename: {}", userId, file.getOriginalFilename());
    try {
      ProfilePictureDTO updatedPicture =
          profilePictureService.saveOrUpdateProfilePicture(userId, file);
      logger.info("✅ Profile picture updated successfully for userId: {}", userId);
      return ResponseEntity.ok(updatedPicture);
    } catch (DuplicateProfilePictureException e) {
      logger.warn("⚠️ Duplicate profile picture for userId: {}", userId);
      return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));
    } catch (RuntimeException | IOException e) {
      logger.error("❌ Error updating profile picture for userId: {}", userId, e);
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
    }
  }

  @PreAuthorize("#userId == authentication.principal.id")
  @DeleteMapping
  public ResponseEntity<Void> deleteProfilePicture(@PathVariable Long userId) {
    logger.info("🗑️ DELETE profile picture for userId: {}", userId);
    try {
      boolean deleted = profilePictureService.deleteProfilePicture(userId);
      if (deleted) {
        logger.info("✅ Profile picture deleted successfully for userId: {}", userId);
        return ResponseEntity.noContent().build();
      } else {
        logger.warn("⚠️ Profile picture not found for userId: {}", userId);
        return ResponseEntity.notFound().build();
      }
    } catch (Exception e) {
      logger.error("❌ Error deleting profile picture for userId: {}", userId, e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }
}
