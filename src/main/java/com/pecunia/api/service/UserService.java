package com.pecunia.api.service;

import com.pecunia.api.dto.UserDTO;
import com.pecunia.api.mapper.UserMapper;
import com.pecunia.api.model.User;
import com.pecunia.api.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;


    public UserService(
          UserRepository userRepository,
          PasswordEncoder passwordEncoder,
          UserMapper userMapper
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    public User registerUser(String firstname, String lastname, String email, String password, Set<String> roles) {
        if(userRepository.existsByEmail(email)) {
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
        List<User> users = userRepository.findAll();
        return users.stream().map(userMapper::convertToDTO).collect(Collectors.toList());
    }
    
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return null;
        }

        return userMapper.convertToDTO(user);
    }

    public List<UserDTO> getUserByFirstname(String searchTerms) {
      List<User> users = userRepository.findByFirstname(searchTerms);
      return users.stream().map(userMapper::convertToDTO).collect(Collectors.toList());
    }

    public List<UserDTO> getUserByLastname(String searchTerms) {
      List<User> users = userRepository.findByLastname(searchTerms);
      return users.stream().map(userMapper::convertToDTO).collect(Collectors.toList());
    }

    public UserDTO updateUser(Long id, User userDetails) {

        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            return null;
        }

        System.out.println("Received user details: " + userDetails);
      System.out.println("Received user details: " + userDetails.getFirstname());

        if (userDetails.getFirstname() != null) {
          user.setFirstname(userDetails.getFirstname());
        }

        if (userDetails.getLastname() != null) {
          user.setLastname(userDetails.getLastname());
        }

        if (userDetails.getProfilePicture() != null) {
          user.setProfilePicture(userDetails.getProfilePicture());
        }

        if (userDetails.getEmail() != null) {
          user.setEmail(userDetails.getEmail());
        }
 
        User updatedUser = userRepository.save(user);
        return userMapper.convertToDTO(updatedUser);
    }

    public boolean deleteUserById(Long id) {
        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            return false;
        }

        userRepository.delete(user);
        return true;
    }
}
