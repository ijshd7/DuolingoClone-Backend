package com.testingpractice.duoclonebackend.dto;

public record ExerciseAttemptResponse(
        boolean correct,
        int score,
        String message
) {
}
