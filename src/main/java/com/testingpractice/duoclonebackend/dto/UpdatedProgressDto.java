package com.testingpractice.duoclonebackend.dto;

import com.testingpractice.duoclonebackend.entity.UserCourseProgress;

import java.util.List;

public record UpdatedProgressDto(
    UserCourseProgress userCourseProgress, List<Integer> markedLessonIds) {}
