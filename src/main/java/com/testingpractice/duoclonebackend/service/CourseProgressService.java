package com.testingpractice.duoclonebackend.service;

import com.testingpractice.duoclonebackend.dto.CourseDTO;
import com.testingpractice.duoclonebackend.dto.LessonDto;
import com.testingpractice.duoclonebackend.entity.Course;
import com.testingpractice.duoclonebackend.entity.Lesson;
import java.util.List;

public interface CourseProgressService {

  List<LessonDto> updateUsersNextLesson(Integer userId, Integer courseId, Lesson currentLesson, boolean isCompleted, Integer scoreForLesson);

  Integer getLessonSectionId(Integer lessonId);

  List<Course> getUserCourses(Integer userId);

}
