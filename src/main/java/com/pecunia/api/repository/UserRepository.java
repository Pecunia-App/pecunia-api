package com.pecunia.api.repository;

import com.pecunia.api.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmail(String email);

  boolean existsByEmail(String email);

  List<User> findByFirstname(String firstname);

  List<User> findByLastname(String lastname);
}
