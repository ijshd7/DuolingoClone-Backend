package com.testingpractice.duoclonebackend.mapper;


import com.testingpractice.duoclonebackend.dto.ExerciseOptionDto;
import com.testingpractice.duoclonebackend.entity.ExerciseOption;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExerciseOptionMapper {

    ExerciseOptionDto toDto(ExerciseOption option);

    List<ExerciseOptionDto> toDtoList(List<ExerciseOption> options);
}