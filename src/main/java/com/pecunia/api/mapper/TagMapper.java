package com.pecunia.api.mapper;

import com.pecunia.api.dto.tag.TagDto;
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
}
