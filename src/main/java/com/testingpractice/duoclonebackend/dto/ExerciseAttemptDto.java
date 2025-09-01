package com.testingpractice.duoclonebackend.dto;

import java.time.LocalDateTime;

public record ExerciseAttemptDto(
        Integer id,
        Integer exerciseId,
        Integer userId,
        LocalDateTime submittedAt,
        Integer optionId,
        Integer score
) {}
