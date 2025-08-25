package com.pecunia.api.controller;

import com.pecunia.api.dto.tag.TagDto;
import com.pecunia.api.dto.tag.TagRequestDto;
import com.pecunia.api.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tags")
public class TagController {
  private final TagService tagService;

  public TagController(TagService tagService) {
    this.tagService = tagService;
  }

  @GetMapping
  @Operation(summary = "Return all tags with pagination", description = "Role Admin require")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity<Page<TagDto>> getAllTags(Pageable pageable) {
    Page<TagDto> tags = tagService.getAll(pageable);
    return tags.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(tags);
  }

  @GetMapping("/users/{userId}")
  @Operation(
      summary = "Return all tags inside a wallet id",
      description = "User id or Role Admin require")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity<Page<TagDto>> getAllTagsByUser(Long userId, Pageable pageable) {
    Page<TagDto> tags = tagService.getUserTags(userId, pageable);
    return tags.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(tags);
  }

  @GetMapping("/{id}")
  @Operation(
      summary = "Get a specific Tag by id",
      description = "Role Admin require or login with correct user id")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity<TagDto> getById(@PathVariable Long id) {
    TagDto tag = tagService.getTagById(id);
    return tag == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(tag);
  }

  @PostMapping
  @Operation()
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity<TagRequestDto> createTag(@Valid @RequestBody TagRequestDto tag) {
    TagRequestDto savedTag = tagService.create(tag);
    return ResponseEntity.status(HttpStatus.CREATED).body(savedTag);
  }

  @PutMapping("/{id}")
  @Operation()
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity<TagRequestDto> updateTag(
      @PathVariable Long id, @Valid @RequestBody TagRequestDto tagRequestDto) {
    TagRequestDto updateTag = tagService.udpate(id, tagRequestDto);
    return updateTag == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(updateTag);
  }
}
