package com.testingpractice.duoclonebackend.dto;

public record LessonCompleteResponse(
        Integer totalScore,
        Integer lessonId,
        String message

) {
}
