package com.testingpractice.duoclonebackend.service;

import com.testingpractice.duoclonebackend.dto.ExerciseDto;
import com.testingpractice.duoclonebackend.entity.Exercise;
import com.testingpractice.duoclonebackend.repository.ExerciseAttemptOptionRepository;
import com.testingpractice.duoclonebackend.repository.ExerciseAttemptRepository;
import com.testingpractice.duoclonebackend.repository.ExerciseOptionRepository;
import com.testingpractice.duoclonebackend.repository.ExerciseRepository;
import jakarta.transaction.Transactional;

import java.util.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExerciseServiceImpl implements ExerciseService {

  private final ExerciseRepository exerciseRepository;
  private final ExerciseAttemptRepository exerciseAttemptRepository;
  private final ExerciseOptionServiceImpl exerciseOptionServiceImpl;

  @Override
  @Transactional
  public List<ExerciseDto> getExercisesForLesson(Integer lessonId, Integer userId) {

    exerciseAttemptRepository.markUncheckedByUserAndLesson(userId, lessonId);

    List<Exercise> exercises = new ArrayList<>(exerciseRepository.findAllByLessonId(lessonId));
    exercises.sort(Comparator.comparingInt(Exercise::getOrderIndex));

    List<ExerciseDto> exerciseDtosWithRandomizedOptionOrder = exerciseOptionServiceImpl.getRandomizedExercisesForLesson(exercises, userId);
    return exerciseDtosWithRandomizedOptionOrder;

  }



}
