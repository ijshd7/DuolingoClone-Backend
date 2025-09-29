package com.testingpractice.duoclonebackend.dto;

public record UserCourseProgressDto(
    Integer id,
    Integer userId,
    Integer courseId,
    Integer sectionId,
    Boolean isComplete,
    Integer currentLessonId,
    Integer completedLessons) {}
