package com.pecunia.api.service;

import com.pecunia.api.dto.tag.TagDto;
import com.pecunia.api.dto.tag.TagRequestDto;
import com.pecunia.api.exception.ResourceNotFoundException;
import com.pecunia.api.mapper.TagMapper;
import com.pecunia.api.model.Tag;
import com.pecunia.api.model.Transaction;
import com.pecunia.api.repository.TagRepository;
import java.time.LocalDateTime;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TagService {
  private final TagRepository tagRepository;
  private final TagMapper tagMapper;

  public TagService(TagRepository tagRepository, TagMapper tagMapper) {
    this.tagRepository = tagRepository;
    this.tagMapper = tagMapper;
  }

  private static void validateTagCreation(TagRequestDto tagCreateDto) {
    if (tagCreateDto.getTagName() == null) {
      throw new IllegalArgumentException("Tag name cannot be null.");
    }
  }

  /**
   * Pagination of all tags, Useful for Admin.
   *
   * @param pageable PageRequest
   * @return Paginaton of tags
   */
  public Page<TagDto> getAll(@ParameterObject Pageable pageable) {
    Page<Tag> tags = tagRepository.findAll(pageable);
    return tags.map(tagMapper::convertToDto);
  }

  /**
   * Pagination of all tags from an User.
   *
   * @param userId user id
   * @param pageable {@link Pageable}
   * @return Pagination of tagDto
   */
  public Page<TagDto> getUserTags(Long userId, Pageable pageable) {
    Page<Tag> tags = tagRepository.findByTransactionsWalletUserId(userId, pageable);
    return tags.map(tagMapper::convertToDto);
  }

  /**
   * Search by tag name.
   *
   * @param userId user id
   * @param searchTerm search term
   * @param pageable {@link Pageable}
   * @return Pagination of tagDto
   */
  public Page<TagDto> searchTags(Long userId, String searchTerm, Pageable pageable) {
    Page<Tag> tags =
        tagRepository.findByTagNameContainingIgnoreCaseAndTransactionsWalletUserId(
            searchTerm, userId, pageable);
    return tags.map(tagMapper::convertToDto);
  }

  /**
   * Create a tag.
   *
   * @param tagCreateDto dto for creating
   * @return new tag
   */
  public TagRequestDto create(TagRequestDto tagCreateDto) {
    validateTagCreation(tagCreateDto);
    Tag tag = tagMapper.convertCreateDtoToEntity(tagCreateDto);
    Tag savedTag = tagRepository.save(tag);
    return tagMapper.convertToCreateDto(savedTag);
  }

  /**
   * Update a tag.
   *
   * @param id tag id
   * @param tagUpdateDto dto for updating
   * @return updated tag
   */
  public TagRequestDto udpate(Long id, TagRequestDto tagUpdateDto) {
    Tag tag = getTagByIdOrThrow(id);
    if (tag == null) {
      throw new InvalidDataAccessApiUsageException(
          "Invalid entity: cannot save null or invalid entities");
    }
    if (tagUpdateDto.getTagName() != null) {
      tag.setTagName(tagUpdateDto.getTagName());
    }
    tag.setUpdatedAt(LocalDateTime.now());
    Tag updatedTag = tagRepository.save(tag);
    return tagMapper.convertToCreateDto(updatedTag);
  }

  /**
   * Delete a tag.
   *
   * @param id tag id
   */
  public boolean delete(Long id) {
    Tag tag = getTagByIdOrThrow(id);
    if (tag == null) {
      return false;
    }
    if (tag.getTransactions() != null) {
      for (Transaction transaction : tag.getTransactions()) {
        transaction.getTags().remove(tag);
      }
      tag.getTransactions().clear();
    }
    tagRepository.delete(tag);
    return true;
  }

  /**
   * A tag by id.
   *
   * @param id tag id
   * @return a single tag
   */
  public TagDto getTagById(Long id) {
    Tag tag = getTagByIdOrThrow(id);
    return tag != null ? tagMapper.convertToDto(tag) : null;
  }

  private Tag getTagByIdOrThrow(Long tagId) {
    return tagRepository
        .findById(tagId)
        .orElseThrow(
            () ->
                new ResourceNotFoundException(
                    "Le tag avec l'id " + tagId + " n'a pas été trouvé."));
  }
}
