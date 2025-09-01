package com.testingpractice.duoclonebackend.mapper;

import com.testingpractice.duoclonebackend.dto.SectionDto;
import com.testingpractice.duoclonebackend.entity.Section;
import org.mapstruct.Mapper;

import java.util.List;
@Mapper(componentModel = "spring")

public interface SectionMapper {

    SectionDto toDto (Section section);
    List<SectionDto> toDtoList(List<Section> sections);

}
