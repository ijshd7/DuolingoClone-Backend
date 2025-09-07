package com.testingpractice.duoclonebackend.dto;

public record UserCourseProgressDto(
    Integer id,
    Integer userId,
    Integer courseId,
    Integer currentLessonId,
    Integer completedLessons) {}
