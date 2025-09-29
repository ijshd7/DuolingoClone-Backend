package com.testingpractice.duoclonebackend.service;

import com.testingpractice.duoclonebackend.dto.LessonDto;
import com.testingpractice.duoclonebackend.dto.NextLessonDto;
import com.testingpractice.duoclonebackend.entity.Lesson;
import com.testingpractice.duoclonebackend.entity.Unit;
import com.testingpractice.duoclonebackend.entity.UserCourseProgress;
import com.testingpractice.duoclonebackend.exception.ApiException;
import com.testingpractice.duoclonebackend.exception.ErrorCode;
import com.testingpractice.duoclonebackend.mapper.LessonMapper;
import com.testingpractice.duoclonebackend.repository.LessonCompletionRepository;
import com.testingpractice.duoclonebackend.repository.UserCourseProgressRepository;
import jakarta.transaction.Transactional;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseProgressServiceImpl implements CourseProgressService {

  private final UserCourseProgressRepository userCourseProgressRepository;
  private final CurriculumNavigator curriculumNavigator;
  private final LookupService lookupService;
  private final LessonCompletionRepository lessonCompletionRepository;
  private final LessonMapper lessonMapper;

  @Override
  @Transactional
  public List<LessonDto> updateUsersNextLesson(
      Integer userId, Integer courseId,  Lesson currentLesson, boolean isCompleted, Integer scoreForLesson) {
    UserCourseProgress userCourseProgress =
        userCourseProgressRepository.findByUserIdAndCourseId(userId, courseId);

    List<LessonDto> lessonsPassed = new ArrayList<>();

    if (userCourseProgress == null) throw new ApiException(ErrorCode.USER_NOT_FOUND);

    Integer userCourseProgressCurrentLessonId = userCourseProgress.getCurrentLessonId();
    boolean isCurrentCourseLesson = userCourseProgressCurrentLessonId.equals(currentLesson.getId());

    boolean isCourseCompleted = userCourseProgress.getIsComplete();

    if (!isCompleted) {
      NextLessonDto nextLessonDto = curriculumNavigator.getNextLesson(currentLesson, userId, courseId);
      if (nextLessonDto.isCourseCompleted()) {
        isCourseCompleted = true;
        userCourseProgress.setIsComplete(true);
      }

      Lesson nextLesson = nextLessonDto.nextLesson();

      if (isCourseCompleted) {
        userCourseProgress.setCurrentLessonId(currentLesson.getId());
      } else if (nextLesson != null) {
        userCourseProgress.setCurrentLessonId(nextLesson.getId());
      } else {
        throw new ApiException(ErrorCode.LESSON_NOT_FOUND);
      }

    }

    if (!isCompleted && !isCurrentCourseLesson && !isCourseCompleted) {
      Lesson currentCourseProgressLesson = lookupService.lessonOrThrow(userCourseProgressCurrentLessonId);
      List<Lesson> skippedLessons = curriculumNavigator.getLessonsBetweenInclusive(courseId, currentCourseProgressLesson, currentLesson, userId);
      Timestamp now = Timestamp.from((Instant.now()));
      for (Lesson skippedLesson : skippedLessons) {
        if (!skippedLesson.getId().equals(currentLesson.getId())) {
            lessonCompletionRepository.insertIfAbsent(userId, skippedLesson.getId(), courseId, 0, now);
            lessonsPassed.add(lessonMapper.toDto(skippedLesson, true));
        }
      }
    }

    userCourseProgressRepository.save(userCourseProgress);

    return lessonsPassed;
  }

  @Override
    public List<Integer> getUserCourseIds(Integer userId) {
        List<UserCourseProgress> allUserCourseProgresses = userCourseProgressRepository.findAllByUserId(userId);
        List<Integer> courseIds = new ArrayList<>();
        for (UserCourseProgress progress : allUserCourseProgresses) {
            courseIds.add(progress.getCourseId());
        }
        return courseIds;
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
