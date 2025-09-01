package com.testingpractice.duoclonebackend.mapper;

import com.testingpractice.duoclonebackend.dto.LessonDto;
import com.testingpractice.duoclonebackend.entity.Lesson;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LessonMapper {

    LessonDto toDto (Lesson lesson);
    List<LessonDto> toDtoList(List<Lesson> lessons);

}
