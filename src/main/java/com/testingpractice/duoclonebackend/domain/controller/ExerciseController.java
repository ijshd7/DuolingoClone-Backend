package com.testingpractice.duoclonebackend.domain.controller;

import com.testingpractice.duoclonebackend.constants.pathConstants;
import com.testingpractice.duoclonebackend.dto.ExerciseAttemptRequest;
import com.testingpractice.duoclonebackend.dto.ExerciseAttemptResponse;
import com.testingpractice.duoclonebackend.service.ExerciseService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(pathConstants.EXERCISES)
public class ExerciseController {

  private final ExerciseService exerciseService;

  public ExerciseController(ExerciseService exerciseService) {
    this.exerciseService = exerciseService;
  }



}
