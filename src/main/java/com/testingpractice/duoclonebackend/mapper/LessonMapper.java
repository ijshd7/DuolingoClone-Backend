package com.testingpractice.duoclonebackend.mapper;

import com.testingpractice.duoclonebackend.dto.LessonDto;
import com.testingpractice.duoclonebackend.entity.Lesson;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface LessonMapper {
    @Mapping(target = "isPassed", expression = "java(passed)")
    LessonDto toDto(Lesson lesson, boolean passed);

    default List<LessonDto> toDtoList(List<Lesson> lessons, Set<Integer> completedIds) {
        return lessons.stream()
                .map(l -> toDto(l, completedIds.contains(l.getId())))
                .toList();
    }
}