package com.pecunia.api.mapper;

import com.pecunia.api.dto.tag.TagDto;
import com.pecunia.api.dto.tag.TagRequestDto;
import com.pecunia.api.model.Tag;
import com.pecunia.api.model.User;
import org.springframework.stereotype.Component;

/** TagMapper. */
@Component
public class TagMapper {
  /**
   * @param tag
   * @return
   */
  public TagDto convertToDto(Tag tag) {
    TagDto dto = new TagDto();
    dto.setId(tag.getId());
    dto.setTagName(tag.getTagName());
    dto.setUpdatedAt(tag.getUpdatedAt());
    dto.setCreatedAt(tag.getCreatedAt());

    return dto;
  }

  public Tag convertCreateDtoToEntity(TagRequestDto dto, User user) {
    Tag tag = new Tag();
    tag.setTagName(dto.getTagName());
    tag.setUser(user);
    return tag;
  }

  public TagRequestDto convertToCreateDto(Tag tag) {
    TagRequestDto dto = new TagRequestDto();
    dto.setTagName(tag.getTagName());
    return dto;
  }
}
