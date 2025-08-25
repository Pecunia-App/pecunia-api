package com.pecunia.api.config;

import com.pecunia.api.model.User;
import com.pecunia.api.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Profile("dev")
public class DevDataInitializater {

  @Bean
  public CommandLineRunner initUserTest(
      UserRepository userRepository, PasswordEncoder passwordEncoder) {
    return args -> {
      if (!userRepository.existsByEmail("test@pecunia.fr")) {
        User user = new User();
        user.setFirstname("Test");
        user.setLastname("Pecunia");
        user.setEmail("test@pecunia.fr");
        user.setPassword(passwordEncoder.encode("Password123!"));
        user.getRoles().add("ROLE_USER");
        userRepository.save(user);

        System.out.println("Utilisateur de test créé : " + user.getEmail());
      } else {
        User existingUser = userRepository.findByEmail("test@pecunia.fr").get();
        System.out.println("L'utilisateur de test existe déjà : " + existingUser.getEmail());
      }
    };
  }
}
