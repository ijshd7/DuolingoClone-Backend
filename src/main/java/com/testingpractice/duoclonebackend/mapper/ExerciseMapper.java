package com.testingpractice.duoclonebackend.mapper;

import com.testingpractice.duoclonebackend.dto.ExerciseDto;
import com.testingpractice.duoclonebackend.entity.Exercise;
import com.testingpractice.duoclonebackend.entity.ExerciseOption;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = ExerciseOptionMapper.class)
public interface ExerciseMapper {

    @Mapping(target = "id",         source = "exercise.id")
    @Mapping(target = "lessonId",   source = "exercise.lessonId")
    @Mapping(target = "prompt",     source = "exercise.prompt")
    @Mapping(target = "type",       source = "exercise.type")
    @Mapping(target = "orderIndex", source = "exercise.orderIndex")
    @Mapping(target = "options",    source = "options")
    ExerciseDto toDto(Exercise exercise, List<ExerciseOption> options);
}