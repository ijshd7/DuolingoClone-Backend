package com.testingpractice.duoclonebackend.domain.controller;

import com.testingpractice.duoclonebackend.constants.pathConstants;
import com.testingpractice.duoclonebackend.dto.ExerciseAttemptRequest;
import com.testingpractice.duoclonebackend.dto.ExerciseAttemptResponse;
import com.testingpractice.duoclonebackend.service.ExerciseAttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(pathConstants.EXERCISES_ATTEMPTS)
public class ExerciseAttemptController {

  private final ExerciseAttemptService exerciseAttemptService;

  @PostMapping(pathConstants.SUBMIT_EXERCISE)
  public ExerciseAttemptResponse submitExerciseAttempt(
      @RequestBody ExerciseAttemptRequest exerciseAttemptRequest
      // Add Later Authentication Auth
      ) {
    return exerciseAttemptService.submitExerciseAttempt(
        exerciseAttemptRequest.exerciseId(),
        exerciseAttemptRequest.optionIds(),
        exerciseAttemptRequest.userId());
  }
}
