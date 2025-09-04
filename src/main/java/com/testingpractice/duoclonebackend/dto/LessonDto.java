package com.testingpractice.duoclonebackend.dto;

import java.util.List;

public record LessonDto(
        Integer id,
        String title,
        Integer unitId,
        Integer orderIndex,
        String lessonType,
        boolean isPassed
) {}
