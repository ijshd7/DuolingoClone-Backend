package com.testingpractice.duoclonebackend.service;

import com.testingpractice.duoclonebackend.dto.LessonCompleteResponse;
import com.testingpractice.duoclonebackend.dto.LessonDto;
import com.testingpractice.duoclonebackend.entity.*;
import com.testingpractice.duoclonebackend.exception.ApiException;
import com.testingpractice.duoclonebackend.exception.ErrorCode;
import com.testingpractice.duoclonebackend.mapper.LessonMapper;
import com.testingpractice.duoclonebackend.mapper.UserCourseProgressMapper;
import com.testingpractice.duoclonebackend.repository.*;
import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {

  private final LessonRepository lessonRepository;
  private final LessonMapper lessonMapper;
  private final UserCourseProgressRepository userCourseProgressRepository;
  private final UserRepository userRepository;
  private final ExerciseAttemptRepository exerciseAttemptRepository;
  private final UnitRepository unitRepository;
  private final SectionRepository sectionRepository;
  private final ExerciseRepository exerciseRepository;
  private final LessonCompletionRepository lessonCompletionRepository;
  private final UserCourseProgressMapper userCourseProgressMapper;


  public List<LessonDto> getLessonsByUnit(Integer unitId, Integer userId) {
    List<Lesson> lessons = lessonRepository.findAllByUnitId(unitId);
    return lessonMapper.toDtoList(
        lessons, completedSetFor(userId, lessons.stream().map(Lesson::getId).toList()));
  }

  public List<LessonDto> getLessonsByIds(List<Integer> lessonIds, Integer userId) {
    List<Lesson> lessons = lessonRepository.findAllById(lessonIds);
    return lessonMapper.toDtoList(
        lessons, completedSetFor(userId, lessons.stream().map(Lesson::getId).toList()));
  }

  public List<Integer> getLessonIdsByUnit(Integer unitId) {
    List<Integer> lessonIds = lessonRepository.findAllLessonIdsByUnitId(unitId);
    return lessonIds;
  }

  public Set<Integer> completedSetFor(Integer userId, List<Integer> lessonIds) {
    if (lessonIds.isEmpty()) return Set.of();
    return new HashSet<>(lessonCompletionRepository.findCompletedLessonIdsIn(userId, lessonIds));
  }

  @Transactional
  public LessonCompleteResponse getCompletedLesson(Integer lessonId, Integer userId, Integer courseId) {

    UserCourseProgress userCourseProgress =
        userCourseProgressRepository.findByUserIdAndCourseId(userId, courseId);
    if (userCourseProgress == null)
      throw new ApiException(ErrorCode.USER_NOT_FOUND);

    Optional<User> optionalUser = userRepository.findById(userId);
    if (optionalUser.isEmpty())
      throw new ApiException(ErrorCode.USER_NOT_FOUND);
    User user = optionalUser.get();

    List<Exercise> lessonExercises = exerciseRepository.findAllByLessonId(lessonId);
    List<Integer> exerciseIds = lessonExercises.stream().map(Exercise::getId).toList();
    List<ExerciseAttempt> exerciseAttempts =
            exerciseAttemptRepository.findAllByExerciseIdInAndUserIdAndUnchecked(exerciseIds, user.getId());

    Integer scoreForLesson = getLessonPoints(exerciseAttempts);
    user.setPoints(user.getPoints() + scoreForLesson);
    Integer lessonAccuracy = getLessonAccuracy(exerciseAttempts);
    exerciseAttemptRepository.markUncheckedByUserAndLesson(userId, lessonId);

    Optional<Lesson> optionalLesson = lessonRepository.findById(lessonId);
    if (optionalLesson.isEmpty())
      throw new ApiException(ErrorCode.LESSON_NOT_FOUND);
    Lesson lesson = optionalLesson.get();

    if (userCourseProgress.getCurrentLessonId().equals(lessonId)) {

      // UPDATE USERS CURRENT LESSON
      Lesson nextLesson = getNextLesson(lesson, userId, courseId);
      if (nextLesson == null)
        throw new ApiException(ErrorCode.LESSON_NOT_FOUND);
      userCourseProgress.setCurrentLessonId(nextLesson.getId());
    }

    lessonCompletionRepository.insertIfAbsent(
        userId, lessonId, courseId, 15, Timestamp.from((Instant.now())));

    userRepository.save(user);

    userCourseProgressRepository.save(userCourseProgress);

    Integer completedLessonsInCourse =
        lessonCompletionRepository.countByUserAndCourse(userId, courseId);
    if (completedLessonsInCourse == null) completedLessonsInCourse = 0;

    LessonCompleteResponse response =
        new LessonCompleteResponse(
            scoreForLesson,
            lessonAccuracy,
            lessonId,
            lessonMapper.toDto(
                lesson, lessonCompletionRepository.existsByIdUserIdAndIdLessonId(userId, lessonId)),
            userCourseProgressMapper.toDto(userCourseProgress, completedLessonsInCourse),
            lessonAccuracyMessage(lessonAccuracy));

    return response;
  }

  private String lessonAccuracyMessage (Integer accuracy) {

    if (accuracy <= 30) {
      return "OK";
    } else if (accuracy <= 60) {
      return "GOOD";
    } else if (accuracy <= 80) {
      return "GREAT";
    } else {
      return "AMAZING";
    }

  }

  private Integer getLessonPoints (List<ExerciseAttempt> exerciseAttempts) {
    return exerciseAttempts.stream()
            .mapToInt(ExerciseAttempt::getScore)
            .sum();
  }

  private Integer getLessonAccuracy(List<ExerciseAttempt> exerciseAttempts) {
    int earned = exerciseAttempts.stream()
            .mapToInt(ExerciseAttempt::getScore)
            .sum();

    int max = exerciseAttempts.size() * 5;
    int accuracyPercent = (max == 0) ? 0 : (int) ((double) earned / max * 100);

    return accuracyPercent;
  }

  @Nullable
  private Lesson getNextLesson(Lesson lesson, Integer userId, Integer courseId) {

    //GET NEXT LESSON IN UNIT
    Lesson nextLessonInUnit =
        lessonRepository.findFirstByUnitIdAndOrderIndexGreaterThanOrderByOrderIndexAsc(
            lesson.getUnitId(), lesson.getOrderIndex());
    if (nextLessonInUnit != null) return nextLessonInUnit;

    Optional<Unit> currentUnit = unitRepository.findById(lesson.getUnitId());
    if (currentUnit.isEmpty())
      throw new ApiException(ErrorCode.UNIT_NOT_FOUND);

    Unit nextUnit =
        unitRepository.findFirstBySectionIdAndOrderIndexGreaterThanOrderByOrderIndexAsc(
            currentUnit.get().getSectionId(), currentUnit.get().getOrderIndex());
    if (nextUnit != null) {
      return lessonRepository.findFirstByUnitIdOrderByOrderIndexAsc(nextUnit.getId());
    }

    Optional<Section> currentSection = sectionRepository.findById(currentUnit.get().getSectionId());
    if (currentSection.isEmpty())
      throw new ApiException(ErrorCode.SECTION_NOT_FOUND);

    Section nextSection =
        sectionRepository.findFirstByCourseIdAndOrderIndexGreaterThanOrderByOrderIndexAsc(
            currentSection.get().getCourseId(), currentSection.get().getOrderIndex());

    if (nextSection != null) {
      Unit firstUnitOfSection =
          unitRepository.findFirstBySectionIdOrderByOrderIndexAsc(nextSection.getId());
      if (firstUnitOfSection == null)
        throw new ApiException(ErrorCode.COURSE_END);
      return lessonRepository.findFirstByUnitIdOrderByOrderIndexAsc(firstUnitOfSection.getId());
    }

    return null;
  }

}
