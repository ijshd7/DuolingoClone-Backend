package com.testingpractice.duoclonebackend.service;

import com.testingpractice.duoclonebackend.entity.Lesson;
import com.testingpractice.duoclonebackend.entity.LessonCompletion;
import com.testingpractice.duoclonebackend.entity.Unit;
import com.testingpractice.duoclonebackend.entity.UserCourseProgress;
import com.testingpractice.duoclonebackend.exception.ApiException;
import com.testingpractice.duoclonebackend.exception.ErrorCode;
import com.testingpractice.duoclonebackend.repository.LessonCompletionRepository;
import com.testingpractice.duoclonebackend.repository.LessonRepository;
import com.testingpractice.duoclonebackend.repository.UserCourseProgressRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseProgressServiceImpl implements CourseProgressService {

  private final UserCourseProgressRepository userCourseProgressRepository;
  private final CurriculumNavigator curriculumNavigator;
  private final LookupService lookupService;
  private final LessonCompletionRepository lessonCompletionRepository;

  @Override
  @Transactional
  public UserCourseProgress updateUsersNextLesson(
      Integer userId, Integer courseId,  Lesson currentLesson, boolean isCompleted) {
    UserCourseProgress userCourseProgress =
        userCourseProgressRepository.findByUserIdAndCourseId(userId, courseId);

    if (userCourseProgress == null) throw new ApiException(ErrorCode.USER_NOT_FOUND);

    Integer userCourseProgressCurrentLessonId = userCourseProgress.getCurrentLessonId();
    boolean isCurrentCourseLesson = userCourseProgressCurrentLessonId.equals(currentLesson.getId());

    if (!isCompleted) {
      Lesson nextLesson = curriculumNavigator.getNextLesson(currentLesson, userId, courseId);
      if (nextLesson == null) throw new ApiException(ErrorCode.LESSON_NOT_FOUND);
      userCourseProgress.setCurrentLessonId(nextLesson.getId());
    }

    if (!isCompleted && !isCurrentCourseLesson) {
      Lesson currentCourseProgressLesson = lookupService.lessonOrThrow(userCourseProgressCurrentLessonId);
      List<Lesson> skippedLessons = curriculumNavigator.getLessonsBetweenInclusive(courseId, currentCourseProgressLesson, currentLesson, userId);
      Timestamp now = Timestamp.from((Instant.now()));
      for (Lesson skippedLesson : skippedLessons) {
        lessonCompletionRepository.insertIfAbsent(userId, skippedLesson.getId(), courseId, 15, now);
      }
    }

    userCourseProgressRepository.save(userCourseProgress);

    return userCourseProgress;
  }

  @Override
  public Integer getLessonSectionId(Integer lessonId) {
    Lesson lesson = lookupService.lessonOrThrow(lessonId);
    Integer unitId = lesson.getUnitId();
    Unit unit = lookupService.unitOrThrow(unitId);
    Integer sectionId = unit.getSectionId();
    return sectionId;
  }
}
