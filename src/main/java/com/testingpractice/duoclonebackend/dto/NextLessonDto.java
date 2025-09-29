package com.testingpractice.duoclonebackend.dto;

import com.testingpractice.duoclonebackend.entity.Lesson;

public record NextLessonDto(Lesson nextLesson, boolean isCourseCompleted) {}
