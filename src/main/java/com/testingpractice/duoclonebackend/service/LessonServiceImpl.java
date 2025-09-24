package com.testingpractice.duoclonebackend.service;

import com.testingpractice.duoclonebackend.dto.LessonCompleteResponse;
import com.testingpractice.duoclonebackend.dto.LessonDto;
import com.testingpractice.duoclonebackend.dto.NewStreakCount;
import com.testingpractice.duoclonebackend.entity.*;
import com.testingpractice.duoclonebackend.exception.ApiException;
import com.testingpractice.duoclonebackend.exception.ErrorCode;
import com.testingpractice.duoclonebackend.mapper.LessonMapper;
import com.testingpractice.duoclonebackend.mapper.UserCourseProgressMapper;
import com.testingpractice.duoclonebackend.repository.*;
import com.testingpractice.duoclonebackend.utils.AccuracyScoreUtils;
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
  private final StreakService streakService;
  private final CourseProgressService courseProgressService;
  private final ExerciseAttemptServiceImpl exerciseAttemptServiceImpl;

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

}
