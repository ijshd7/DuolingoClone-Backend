package com.testingpractice.duoclonebackend.dto;


public record LessonDto(
    Integer id,
    String title,
    Integer unitId,
    Integer orderIndex,
    String lessonType,
    boolean isPassed) {}
