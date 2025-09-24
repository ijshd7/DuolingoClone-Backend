package com.testingpractice.duoclonebackend.service;

import com.testingpractice.duoclonebackend.dto.ExerciseAttemptResponse;
import com.testingpractice.duoclonebackend.dto.ExerciseDto;
import com.testingpractice.duoclonebackend.entity.AttemptOption;
import com.testingpractice.duoclonebackend.entity.Exercise;
import com.testingpractice.duoclonebackend.entity.ExerciseAttempt;
import com.testingpractice.duoclonebackend.entity.ExerciseOption;
import com.testingpractice.duoclonebackend.mapper.ExerciseAttemptMapper;
import com.testingpractice.duoclonebackend.mapper.ExerciseMapper;
import com.testingpractice.duoclonebackend.repository.ExerciseAttemptOptionRepository;
import com.testingpractice.duoclonebackend.repository.ExerciseAttemptRepository;
import com.testingpractice.duoclonebackend.repository.ExerciseOptionRepository;
import com.testingpractice.duoclonebackend.repository.ExerciseRepository;
import jakarta.transaction.Transactional;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExerciseServiceImpl implements ExerciseService {

  private final ExerciseRepository exerciseRepository;
  private final ExerciseOptionRepository exerciseOptionRepository;
  private final ExerciseAttemptRepository exerciseAttemptRepository;
  private final ExerciseAttemptOptionRepository exerciseAttemptOptionRepository;
  private final ExerciseOptionService exerciseOptionService;

  @Transactional
  public List<ExerciseDto> getExercisesForLesson(Integer lessonId, Integer userId) {

    exerciseAttemptRepository.markUncheckedByUserAndLesson(userId, lessonId);

    List<Exercise> exercises = new ArrayList<>(exerciseRepository.findAllByLessonId(lessonId));
    exercises.sort(Comparator.comparingInt(Exercise::getOrderIndex));

    List<ExerciseDto> exerciseDtosWithRandomizedOptionOrder = exerciseOptionService.getRandomizedExercisesForLesson(exercises, userId);
    return exerciseDtosWithRandomizedOptionOrder;

  }



}
