package com.pecunia.api.service;

import com.pecunia.api.dto.ProfilePictureDTO;
import com.pecunia.api.exception.DuplicateProfilePictureException;
import com.pecunia.api.mapper.ProfilePictureMapper;
import com.pecunia.api.model.ProfilePicture;
import com.pecunia.api.model.User;
import com.pecunia.api.repository.ProfilePictureRepository;
import com.pecunia.api.repository.UserRepository;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProfilePictureService {
  private static final int TARGET_SIZE = 128;
  private static final long MAX_FILE_SIZE = 1024 * 1024; // 1 MB

  private final ProfilePictureRepository profilePictureRepository;
  private final UserRepository userRepository;
  private final ProfilePictureMapper profilePictureMapper;

  public ProfilePictureService(
      ProfilePictureRepository profilePictureRepository,
      UserRepository userRepository,
      ProfilePictureMapper profilePictureMapper) {
    this.profilePictureRepository = profilePictureRepository;
    this.userRepository = userRepository;
    this.profilePictureMapper = profilePictureMapper;
  }

  private void validateImageFormat(MultipartFile file) {

    if (file.getSize() > MAX_FILE_SIZE) {
      throw new RuntimeException("Fichier trop volumineux. Taille maximale : 1 MB.");
    }

    String contentType = file.getContentType();
    if (contentType == null) {
      throw new RuntimeException("Impossible de déterminer le type du fichier");
    }

    if (contentType.equals("image/svg+xml")) {
      throw new RuntimeException(
          "Les fichiers SVG ne sont pas acceptés. Veuillez utiliser JPEG ou PNG.");
    }

    List<String> allowedTypes = Arrays.asList("image/jpeg", "image/png");
    if (!allowedTypes.contains(contentType)) {
      throw new RuntimeException("Format de fichier non supporté. Formats acceptés : JPEG, PNG.");
    }
  }

  public byte[] resizeAndValidateImage(MultipartFile file) throws IOException {
    BufferedImage originalImage = ImageIO.read(file.getInputStream());

    // Créer une nouvelle image carrée de 32x32
    BufferedImage resizedImage =
        new BufferedImage(TARGET_SIZE, TARGET_SIZE, BufferedImage.TYPE_INT_RGB);

    Graphics2D g = resizedImage.createGraphics();
    g.setRenderingHint(
        RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
    g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    // Calculer les dimensions pour un recadrage carré
    int size = Math.min(originalImage.getWidth(), originalImage.getHeight());
    int x = (originalImage.getWidth() - size) / 2;
    int y = (originalImage.getHeight() - size) / 2;

    // Dessiner en 32x32
    g.drawImage(originalImage.getSubimage(x, y, size, size), 0, 0, TARGET_SIZE, TARGET_SIZE, null);
    g.dispose();

    // Convertir en bytes
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ImageIO.write(resizedImage, "PNG", baos);
    return baos.toByteArray();
  }

  public ProfilePictureDTO saveProfilePicture(Long userId, MultipartFile file) throws IOException {
    validateImageFormat(file);
    byte[] pictureData = resizeAndValidateImage(file);

    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

    if (user.getProfilePicture() != null) {
      byte[] existingPicture = user.getProfilePicture().getPicture();

      String newHash = DigestUtils.md5DigestAsHex(pictureData);
      String existingHash = DigestUtils.md5DigestAsHex(existingPicture);

      if (newHash.equals(existingHash)) {
        throw new DuplicateProfilePictureException("L'image est identique à l'existante");
      } else {
        throw new RuntimeException(
            "L'utilisateur possède déjà une photo de profil. Utilisez la fonction de mise à jour.");
      }
    }

    ProfilePicture profilePicture = new ProfilePicture();
    profilePicture.setPicture(pictureData);
    profilePicture.setUser(user);
    profilePicture = profilePictureRepository.save(profilePicture);

    user.setProfilePicture(profilePicture);
    userRepository.save(user);

    return profilePictureMapper.convertToDTO(profilePicture);
  }

  public ProfilePictureDTO getProfilePicture(Long userId) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

    if (user.getProfilePicture() == null) {
      throw new RuntimeException("Photo de profil non trouvée");
    }

    return profilePictureMapper.convertToDTO(user.getProfilePicture());
  }

  public ProfilePictureDTO updateProfilePicture(Long userId, MultipartFile file)
      throws IOException {
    validateImageFormat(file);
    byte[] pictureData = resizeAndValidateImage(file);

    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

    if (user.getProfilePicture() == null) {
      throw new RuntimeException("Aucune photo de profil existante à mettre à jour");
    }

    ProfilePicture profilePicture = user.getProfilePicture();

    String newHash = DigestUtils.md5DigestAsHex(pictureData);
    String existingHash = DigestUtils.md5DigestAsHex(profilePicture.getPicture());
    if (newHash.equals(existingHash)) {
      throw new DuplicateProfilePictureException("L'image est identique à l'existante");
    }

    profilePicture.setPicture(pictureData);
    profilePicture = profilePictureRepository.save(profilePicture);

    return profilePictureMapper.convertToDTO(profilePicture);
  }

  public boolean deleteProfilePicture(@PathVariable Long userId) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new RuntimeException("Utilisateur introuvable."));
    if (user.getProfilePicture() == null) {
      return false;
    }
    ProfilePicture profilePicture = user.getProfilePicture();
    user.setProfilePicture(null);
    userRepository.save(user);
    profilePictureRepository.delete(profilePicture);

    return true;
  }
}
