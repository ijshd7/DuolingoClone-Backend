package com.testingpractice.duoclonebackend.service;

import com.testingpractice.duoclonebackend.dto.UserCourseProgressDto;
import com.testingpractice.duoclonebackend.dto.UserDto;
import com.testingpractice.duoclonebackend.entity.User;
import com.testingpractice.duoclonebackend.entity.UserCourseProgress;
import com.testingpractice.duoclonebackend.exception.ApiException;
import com.testingpractice.duoclonebackend.exception.ErrorCode;
import com.testingpractice.duoclonebackend.mapper.UserCourseProgressMapper;
import com.testingpractice.duoclonebackend.mapper.UserMapper;
import com.testingpractice.duoclonebackend.repository.LessonCompletionRepository;
import com.testingpractice.duoclonebackend.repository.UserCourseProgressRepository;
import com.testingpractice.duoclonebackend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

  private final UserCourseProgressRepository userCourseProgressRepository;
  private final UserCourseProgressMapper userCourseProgressMapper;
  private final LessonCompletionRepository lessonCompletionRepository;
  private final UserRepository userRepository;
  private final UserMapper userMapper;

  public UserServiceImpl(
          UserCourseProgressRepository userCourseProgressRepository,
          UserCourseProgressMapper userCourseProgressMapper,
          LessonCompletionRepository lessonCompletionRepository, UserRepository userRepository, UserMapper userMapper) {
    this.userCourseProgressRepository = userCourseProgressRepository;
    this.userCourseProgressMapper = userCourseProgressMapper;
    this.lessonCompletionRepository = lessonCompletionRepository;
    this.userRepository = userRepository;
    this.userMapper = userMapper;
  }

  public UserCourseProgressDto getUserCourseProgress(Integer courseId, Integer userId) {
    UserCourseProgress userCourseProgress =
        userCourseProgressRepository.findByUserIdAndCourseId(userId, courseId);

    Integer totalLessonCount = lessonCompletionRepository.countByUserAndCourse(userId, courseId);
    if (totalLessonCount == null) totalLessonCount = 0;

    return userCourseProgressMapper.toDto(userCourseProgress, totalLessonCount);
  }

  public UserDto getUser(Integer userId) {
    Optional<User> optionalUser = userRepository.findById(userId);
    if (optionalUser.isEmpty()) {
      throw new ApiException(ErrorCode.USER_NOT_FOUND);
    }

    User user = optionalUser.get();

    Timestamp lastSubmission = user.getLastSubmission();
    if (lastSubmission != null) {

      ZoneId tz = ZoneId.systemDefault(); // or user-specific

      LocalDate today = LocalDate.now(tz);
      LocalDate lastDate = lastSubmission.toInstant().atZone(tz).toLocalDate();
      boolean isOlder = lastDate.isBefore(today.minusDays(1));

      if (isOlder) {
        user.setStreakLength(0);
      }
    }


    return userMapper.toDto(user);
  }

}
