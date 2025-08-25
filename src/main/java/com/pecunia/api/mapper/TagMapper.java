package com.pecunia.api.mapper;

import com.pecunia.api.dto.tag.TagDto;
import com.pecunia.api.dto.tag.TagRequestDto;
import com.pecunia.api.model.Tag;
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

  public Tag convertCreateDtoToEntity(TagRequestDto dto) {
    Tag tag = new Tag();
    tag.setTagName(dto.getTagName());
    return tag;
  }

  public TagRequestDto convertToCreateDto(Tag tag) {
    TagRequestDto dto = new TagRequestDto();
    dto.setTagName(tag.getTagName());
    return dto;
  }
}
