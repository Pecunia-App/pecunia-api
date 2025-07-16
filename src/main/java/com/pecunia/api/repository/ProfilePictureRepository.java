package com.pecunia.api.repository;

import com.pecunia.api.model.ProfilePicture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfilePictureRepository extends JpaRepository<ProfilePicture, Long> {
}
