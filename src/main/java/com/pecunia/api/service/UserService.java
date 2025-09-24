package com.pecunia.api.service;

import com.pecunia.api.dto.UserDTO;
import com.pecunia.api.dto.UserUpdateDTO;
import com.pecunia.api.mapper.UserMapper;
import com.pecunia.api.model.User;
import com.pecunia.api.repository.UserRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserMapper userMapper;

  public UserService(
      UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.userMapper = userMapper;
  }

  public User registerUser(
      String firstname, String lastname, String email, String password, Set<String> roles) {
    if (userRepository.existsByEmail(email)) {
      throw new RuntimeException("Cet email est déjà utilisé");
    }

    User user = new User();
    user.setFirstname(firstname);
    user.setLastname(lastname);
    user.setEmail(email);
    user.setPassword(passwordEncoder.encode(password));
    user.setRoles(roles);

    return userRepository.save(user);
  }

  public List<UserDTO> getAllUsers() {
    return userRepository.findAll().stream()
        .map(userMapper::convertToDTO)
        .collect(Collectors.toList());
  }

  public UserDTO getUserById(Long id) {
    User user = userRepository.findById(id).orElse(null);
    return user != null ? userMapper.convertToDTO(user) : null;
  }

  public List<UserDTO> getUserByFirstname(String searchTerms) {
    return userRepository.findByFirstname(searchTerms).stream()
        .map(userMapper::convertToDTO)
        .collect(Collectors.toList());
  }

  public List<UserDTO> getUserByLastname(String searchTerms) {
    return userRepository.findByLastname(searchTerms).stream()
        .map(userMapper::convertToDTO)
        .collect(Collectors.toList());
  }

  public UserDTO updateUser(Long id, UserUpdateDTO userDetails) {
    User user = userRepository.findById(id).orElse(null);

    if (user == null) {
      return null;
    }

    user.setFirstname(userDetails.getFirstname());
    user.setLastname(userDetails.getLastname());
    user.setEmail(userDetails.getEmail());
    user.setProfilePicture(userDetails.getProfilePicture());

    User updatedUser = userRepository.save(user);
    return userMapper.convertToDTO(updatedUser);
  }

  public boolean deleteUserById(Long id) {
    User user = userRepository.findById(id).orElse(null);
    if (user == null) return false;

    userRepository.delete(user);
    return true;
  }

  /**
   * Met à jour le mot de passe d'un utilisateur.
   *
   * @param userId identifiant utilisateur
   * @param newRawPassword mot de passe en clair reçu du DTO
   */
  @org.springframework.transaction.annotation.Transactional
  public void updatePassword(Long userId, String newRawPassword) {
    if (newRawPassword == null || newRawPassword.isBlank()) {
      throw new IllegalArgumentException("Le nouveau mot de passe est obligatoire.");
    }

    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable"));

    String encoded = passwordEncoder.encode(newRawPassword);
    user.setPassword(encoded);

    userRepository.save(user);

    System.out.println("✅ Mot de passe mis à jour pour l'utilisateur " + userId);
  }
}
