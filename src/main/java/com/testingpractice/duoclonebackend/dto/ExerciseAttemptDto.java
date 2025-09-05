package com.testingpractice.duoclonebackend.dto;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public record ExerciseAttemptDto(
    Integer id,
    Integer exerciseId,
    Integer userId,
    Timestamp submittedAt,
    Integer optionId,
    Integer score,
    Integer isChecked) {}
