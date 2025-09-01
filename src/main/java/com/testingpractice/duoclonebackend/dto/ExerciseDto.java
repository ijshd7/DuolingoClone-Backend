package com.testingpractice.duoclonebackend.dto;
public record ExerciseDto(
        Integer id,
        Integer lessonId,
        String prompt,
        String type,
        Integer orderIndex
) {}