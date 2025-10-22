package com.pecunia.api.repository;

import com.pecunia.api.model.Tag;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

  /**
   * Find Tags from a wallet from an user.
   *
   * @param userId user id
   * @return List tags
   */
  List<Tag> findByUserId(Long userId);

  /**
   * Search by name.
   *
   * @param tagName name of a tag
   * @param userId user id
   * @param pageable page request object
   * @return pagination tags
   */
  Page<Tag> findByTagNameContainingIgnoreCaseAndTransactionsWalletUserId(
      String tagName, Long userId, Pageable pageable);
}
