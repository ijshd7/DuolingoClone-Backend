package com.testingpractice.duoclonebackend.service;

import com.testingpractice.duoclonebackend.dto.LessonDto;
import com.testingpractice.duoclonebackend.entity.*;
import com.testingpractice.duoclonebackend.mapper.LessonMapper;
import com.testingpractice.duoclonebackend.repository.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {

  private final LessonRepository lessonRepository;
  private final LessonMapper lessonMapper;
  private final LessonCompletionRepository lessonCompletionRepository;

  @Override
  public List<LessonDto> getLessonsByUnit(Integer unitId, Integer userId) {
    List<Lesson> lessons = lessonRepository.findAllByUnitId(unitId);
    return lessonMapper.toDtoList(
        lessons, completedSetFor(userId, lessons.stream().map(Lesson::getId).toList()));
  }

  @Override
  public List<LessonDto> getLessonsByIds(List<Integer> lessonIds, Integer userId) {
    List<Lesson> lessons = lessonRepository.findAllById(lessonIds);
    return lessonMapper.toDtoList(
        lessons, completedSetFor(userId, lessons.stream().map(Lesson::getId).toList()));
  }

  @Override
  public List<Integer> getLessonIdsByUnit(Integer unitId) {
    List<Integer> lessonIds = lessonRepository.findAllLessonIdsByUnitId(unitId);
    return lessonIds;
  }

  @Override
  public Set<Integer> completedSetFor(Integer userId, List<Integer> lessonIds) {
    if (lessonIds.isEmpty()) return Set.of();
    return new HashSet<>(lessonCompletionRepository.findCompletedLessonIdsIn(userId, lessonIds));
  }
}
