package com.pecunia.api.repository;

import com.pecunia.api.model.Tag;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/** Tag repository. */
public interface TagRepository extends JpaRepository<Tag, Long> {
  /**
   * Find Tag by Name.
   *
   * @param tagName name of tag
   * @return a tag
   */
  Optional<Tag> findByTagName(String tagName);
}
