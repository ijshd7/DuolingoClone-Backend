package com.testingpractice.duoclonebackend.service;

import com.testingpractice.duoclonebackend.dto.UserCourseProgressDto;
import com.testingpractice.duoclonebackend.entity.UserCourseProgress;
import com.testingpractice.duoclonebackend.mapper.UserCourseProgressMapper;
import com.testingpractice.duoclonebackend.repository.LessonCompletionRepository;
import com.testingpractice.duoclonebackend.repository.UserCourseProgressRepository;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl {

  private final UserCourseProgressRepository userCourseProgressRepository;
  private final UserCourseProgressMapper userCourseProgressMapper;
  private final LessonCompletionRepository lessonCompletionRepository;

  public UserServiceImpl(
          UserCourseProgressRepository userCourseProgressRepository,
          UserCourseProgressMapper userCourseProgressMapper, LessonCompletionRepository lessonCompletionRepository) {
    this.userCourseProgressRepository = userCourseProgressRepository;
    this.userCourseProgressMapper = userCourseProgressMapper;
    this.lessonCompletionRepository = lessonCompletionRepository;
  }

  public UserCourseProgressDto getUserCourseProgress(Integer courseId, Integer userId) {
    UserCourseProgress userCourseProgress =
        userCourseProgressRepository.findByUserIdAndCourseId(userId, courseId);

    Integer totalLessonCount = lessonCompletionRepository.countByUserAndCourse(userId, courseId);
    if (totalLessonCount == null) totalLessonCount = 0;

    return userCourseProgressMapper.toDto(userCourseProgress, totalLessonCount);
  }
}
