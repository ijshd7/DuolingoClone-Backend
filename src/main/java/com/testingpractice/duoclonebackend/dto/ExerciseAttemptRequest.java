package com.testingpractice.duoclonebackend.dto;

public record ExerciseAttemptRequest(
        Integer exerciseId,
        Integer optionId,
        Integer userId
) {
}
