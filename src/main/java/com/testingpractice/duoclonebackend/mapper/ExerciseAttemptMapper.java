package com.testingpractice.duoclonebackend.mapper;

import com.testingpractice.duoclonebackend.dto.ExerciseAttemptDto;
import com.testingpractice.duoclonebackend.entity.ExerciseAttempt;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExerciseAttemptMapper {

    ExerciseAttemptDto toDto(ExerciseAttempt attempt);

    List<ExerciseAttemptDto> toDtoList(List<ExerciseAttempt> attempts);



}