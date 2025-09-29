package com.testingpractice.duoclonebackend.service;

import com.testingpractice.duoclonebackend.dto.UserDto;
import com.testingpractice.duoclonebackend.entity.Course;
import java.util.List;

public interface CourseService {

  UserDto changeUserCourse(Integer userId, Integer newCourseId);

  Integer getFirstLessonIdOfCourse(Integer courseId);

  List<Course> getAllCourses();
}
