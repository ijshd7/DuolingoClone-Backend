package com.testingpractice.duoclonebackend.mapper;

import com.testingpractice.duoclonebackend.dto.SectionDto;
import com.testingpractice.duoclonebackend.entity.Section;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SectionMapper {

  SectionDto toDto(Section section);

  List<SectionDto> toDtoList(List<Section> sections);
}
