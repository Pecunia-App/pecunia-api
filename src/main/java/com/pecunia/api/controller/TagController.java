package com.pecunia.api.controller;

import com.pecunia.api.dto.tag.TagDto;
import com.pecunia.api.dto.tag.TagRequestDto;
import com.pecunia.api.security.HasRole;
import com.pecunia.api.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagController {
  private final TagService tagService;

  public TagController(TagService tagService) {
    this.tagService = tagService;
  }

  @GetMapping
  @Operation(summary = "Return all tags with pagination", description = "Role Admin require")
  @HasRole("ADMIN")
  public ResponseEntity<Page<TagDto>> getAllTags(Pageable pageable) {
    Page<TagDto> tags = tagService.getAll(pageable);
    return tags.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(tags);
  }

  @GetMapping("/users/{userId}")
  @Operation(
      summary = "Return all tags inside a wallet id",
      description = "User id or Role Admin require")
  @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
  public ResponseEntity<List<TagDto>> getAllTagsByUser(@PathVariable Long userId) {
    List<TagDto> tags = tagService.getUserTags(userId);
    return tags.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(tags);
  }

  @GetMapping("/{id}")
  @Operation(
      summary = "Get a specific Tag by id",
      description = "Role Admin require or login with correct user id")
  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  public ResponseEntity<TagDto> getById(@PathVariable Long id) {
    TagDto tag = tagService.getTagById(id);
    return tag == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(tag);
  }

  @PostMapping
  @Operation(
      summary = "Create a tag.",
      description = "Role admin require or login with correct user id.")
  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  public ResponseEntity<TagRequestDto> createTag(@Valid @RequestBody TagRequestDto tag) {
    TagRequestDto savedTag = tagService.create(tag);
    return ResponseEntity.status(HttpStatus.CREATED).body(savedTag);
  }

  @PutMapping("/{id}")
  @Operation(
      summary = "Update a tag.",
      description = "Role admin require or login with correct user id.")
  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  public ResponseEntity<TagRequestDto> updateTag(
      @PathVariable Long id, @Valid @RequestBody TagRequestDto tagRequestDto) {
    TagRequestDto updateTag = tagService.udpate(id, tagRequestDto);
    return updateTag == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(updateTag);
  }

  @DeleteMapping("/{id}")
  @Operation(
      summary = "Delete a tag.",
      description = "Role adming rquire or login with correct user id.")
  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
    return tagService.delete(id)
        ? ResponseEntity.noContent().build()
        : ResponseEntity.notFound().build();
  }
}
