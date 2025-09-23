package com.testingpractice.duoclonebackend.dto;

public record LessonCompleteResponse(
    Integer totalScore,
    Integer newUserScore,
    Integer accuracy,
    Integer lessonId,
    LessonDto updatedLesson,
    UserCourseProgressDto updatedUserCourseProgress,
    NewStreakCount newStreakCount,
    String message) {}
