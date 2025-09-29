package com.testingpractice.duoclonebackend.service;

import com.testingpractice.duoclonebackend.dto.ExerciseDto;
import java.util.List;

public interface ExerciseService {
  List<ExerciseDto> getExercisesForLesson(Integer lessonId, Integer userId);


}
