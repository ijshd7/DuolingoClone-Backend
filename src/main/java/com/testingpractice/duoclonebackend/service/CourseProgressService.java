package com.testingpractice.duoclonebackend.service;

import com.testingpractice.duoclonebackend.dto.LessonDto;
import com.testingpractice.duoclonebackend.entity.Lesson;
import com.testingpractice.duoclonebackend.entity.UserCourseProgress;

import java.util.List;

public interface CourseProgressService {

  List<LessonDto> updateUsersNextLesson(Integer userId, Integer courseId, Lesson currentLesson, boolean isCompleted);

  Integer getLessonSectionId(Integer lessonId);
}
