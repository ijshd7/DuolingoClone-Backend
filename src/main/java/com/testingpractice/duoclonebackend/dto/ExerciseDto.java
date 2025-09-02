package com.testingpractice.duoclonebackend.dto;

import java.util.List;

public record ExerciseDto(
        Integer id,
        Integer lessonId,
        String prompt,
        String type,
        Integer orderIndex,
        List<ExerciseOptionDto> options
) {}