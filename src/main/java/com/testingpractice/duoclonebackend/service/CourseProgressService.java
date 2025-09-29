package com.testingpractice.duoclonebackend.service;

import com.testingpractice.duoclonebackend.entity.Lesson;
import com.testingpractice.duoclonebackend.entity.UserCourseProgress;

public interface CourseProgressService {

    UserCourseProgress updateUsersNextLesson (Integer userId, Integer courseId, Lesson currentLesson);

    public Integer getLessonSectionId (Integer lessonId);

}
