package com.pecunia.api.service;

import com.pecunia.api.dto.ProfilePictureDTO;
import com.pecunia.api.mapper.ProfilePictureMapper;
import com.pecunia.api.model.ProfilePicture;
import com.pecunia.api.model.User;
import com.pecunia.api.repository.ProfilePictureRepository;
import com.pecunia.api.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


@Service
public class ProfilePictureService {
  private static final int TARGET_SIZE = 32;
  private static final long MAX_FILE_SIZE = 50 * 1024; // 50KB suffisant pour 32x32

  private final ProfilePictureRepository profilePictureRepository;
  private final UserRepository userRepository;
  private final ProfilePictureMapper profilePictureMapper;

  public ProfilePictureService(ProfilePictureRepository profilePictureRepository,
                               UserRepository userRepository,
                               ProfilePictureMapper profilePictureMapper) {
    this.profilePictureRepository = profilePictureRepository;
    this.userRepository = userRepository;
    this.profilePictureMapper = profilePictureMapper;
  }

  public byte[] resizeAndValidateImage(MultipartFile file) throws IOException {
    BufferedImage originalImage = ImageIO.read(file.getInputStream());

    // Créer une nouvelle image carrée de 32x32
    BufferedImage resizedImage = new BufferedImage(TARGET_SIZE, TARGET_SIZE, BufferedImage.TYPE_INT_RGB);

    Graphics2D g = resizedImage.createGraphics();
    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

    // Calculer les dimensions pour un recadrage carré
    int size = Math.min(originalImage.getWidth(), originalImage.getHeight());
    int x = (originalImage.getWidth() - size) / 2;
    int y = (originalImage.getHeight() - size) / 2;

    // Dessiner en 32x32
    g.drawImage(originalImage.getSubimage(x, y, size, size),
            0, 0, TARGET_SIZE, TARGET_SIZE, null);
    g.dispose();

    // Convertir en bytes
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ImageIO.write(resizedImage, "JPEG", baos);
    return baos.toByteArray();
  }

  public ProfilePictureDTO saveProfilePicture(Long userId, byte[] pictureData) {
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

    if (user.getProfilePicture() != null) {
      throw new RuntimeException("L'utilisateur possède déjà une photo de profil. Utilisez la fonction de mise à jour à la place.");
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
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

    if (user.getProfilePicture() == null) {
      throw new RuntimeException("Photo de profil non trouvée");
    }

    return profilePictureMapper.convertToDTO(user.getProfilePicture());
  }

  public ProfilePictureDTO updateProfilePicture(Long userId, byte[] pictureData) {
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

    if (user.getProfilePicture() == null) {
      throw new RuntimeException("Aucune photo de profil existante à mettre à jour");
    }

    ProfilePicture profilePicture = user.getProfilePicture();
    profilePicture.setPicture(pictureData);
    profilePicture = profilePictureRepository.save(profilePicture);

    return profilePictureMapper.convertToDTO(profilePicture);
  }

  public boolean deleteProfilePicture(@PathVariable Long userId) {
    User user = userRepository.findById(userId)
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
