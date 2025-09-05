package com.testingpractice.duoclonebackend.dto;

public record LessonCompleteResponse(
    Integer totalScore,
    Integer lessonId,
    LessonDto updatedLesson,
    UserCourseProgressDto updatedUserCourseProgress,
    String message) {}
