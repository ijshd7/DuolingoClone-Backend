package com.testingpractice.duoclonebackend.service;

import com.testingpractice.duoclonebackend.dto.ExerciseAttemptResponse;
import com.testingpractice.duoclonebackend.entity.ExerciseAttempt;

import java.util.ArrayList;
import java.util.List;

public interface ExerciseAttemptService {

  ExerciseAttemptResponse submitExerciseAttempt(
      Integer exerciseId, ArrayList<Integer> optionIds, Integer userId);

  List<ExerciseAttempt> getLessonExerciseAttemptsForUser(Integer lessonId, Integer userId);

  void markAttemptsAsChecked(Integer userId, Integer lessonId);
}
