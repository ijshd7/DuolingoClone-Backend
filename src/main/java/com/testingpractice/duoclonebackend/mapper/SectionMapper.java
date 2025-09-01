package com.testingpractice.duoclonebackend.mapper;

import com.testingpractice.duoclonebackend.dto.SectionDTO;
import com.testingpractice.duoclonebackend.entity.Section;
import org.mapstruct.Mapper;

import java.util.List;
@Mapper(componentModel = "spring")

public interface SectionMapper {

    SectionDTO toDto (Section section);
    List<SectionDTO> toDtoList(List<Section> sections);

}
