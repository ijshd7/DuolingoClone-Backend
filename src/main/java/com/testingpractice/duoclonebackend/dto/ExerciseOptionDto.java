package com.testingpractice.duoclonebackend.dto;

public record ExerciseOptionDto(
    Integer id, Integer exerciseId, String content, String imageUrl, Boolean isCorrect) {}
