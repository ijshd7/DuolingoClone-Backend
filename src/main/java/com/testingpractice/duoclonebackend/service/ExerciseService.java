package com.testingpractice.duoclonebackend.service;

import com.testingpractice.duoclonebackend.dto.ExerciseAttemptDto;
import com.testingpractice.duoclonebackend.dto.ExerciseAttemptResponse;
import com.testingpractice.duoclonebackend.dto.ExerciseDto;

import java.util.List;

public interface ExerciseService {
    List<ExerciseDto> getExercisesForLesson (Integer lessonId);
    ExerciseAttemptResponse submitExerciseAttempt (Integer exerciseId, Integer optionId, Integer userId);
}
