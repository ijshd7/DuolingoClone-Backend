package com.testingpractice.duoclonebackend.dto;

import java.util.List;

public record LessonCompleteResponse(
    Integer userId,
    Integer totalScore,
    Integer newUserScore,
    Integer accuracy,
    Integer lessonId,
    LessonDto updatedLesson,
    List<LessonDto> lessonsToInvalidate,
    UserCourseProgressDto updatedUserCourseProgress,
    NewStreakCount newStreakCount,
    String message) {}
