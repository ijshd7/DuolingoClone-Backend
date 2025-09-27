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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

  private final UserCourseProgressRepository userCourseProgressRepository;
  private final UserCourseProgressMapper userCourseProgressMapper;
  private final LessonCompletionRepository lessonCompletionRepository;
  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final CourseProgressService courseProgressService;

  public UserCourseProgressDto getUserCourseProgress(Integer courseId, Integer userId) {

    UserCourseProgress userCourseProgress =
        userCourseProgressRepository.findByUserIdAndCourseId(userId, courseId);

    Integer totalLessonCount = lessonCompletionRepository.countByUserAndCourse(userId, courseId);
    if (totalLessonCount == null) totalLessonCount = 0;

    Integer lessonSectionId = courseProgressService.getLessonSectionId(userCourseProgress.getCurrentLessonId());

    return userCourseProgressMapper.toDto(userCourseProgress, totalLessonCount, lessonSectionId);

  }

  @Transactional
  public UserDto getUser(Integer userId) {
    Optional<User> optionalUser = userRepository.findById(userId);
    if (optionalUser.isEmpty()) {
      throw new ApiException(ErrorCode.USER_NOT_FOUND);
    }

    User user = optionalUser.get();
    potentiallyResetStreak(user);



    return userMapper.toDto(user);
  }

  @Transactional
  public List<UserDto> getUsersFromIds(List<Integer> userIds) {
    if (userIds == null || userIds.isEmpty()) {
      return List.of();
    }

    return userRepository.findAllById(userIds).stream()
            .peek(this::potentiallyResetStreak)
            .map(userMapper::toDto)
            .toList();
  }

  @Transactional
  public void potentiallyResetStreak (User user) {
    Timestamp lastSubmission = user.getLastSubmission();
    if (lastSubmission != null) {

      ZoneId tz = ZoneId.systemDefault(); // or user-specific

      LocalDate today = LocalDate.now(tz);
      LocalDate lastDate = lastSubmission.toInstant().atZone(tz).toLocalDate();
      boolean isOlder = lastDate.isBefore(today.minusDays(1));

      if (isOlder) {
        user.setStreakLength(0);
        userRepository.save(user);
      }
    }
  }

}
