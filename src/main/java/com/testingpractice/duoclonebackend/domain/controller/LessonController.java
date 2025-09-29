package com.testingpractice.duoclonebackend.domain.controller;

import com.testingpractice.duoclonebackend.constants.pathConstants;
import com.testingpractice.duoclonebackend.dto.ExerciseDto;
import com.testingpractice.duoclonebackend.dto.LessonDto;
import com.testingpractice.duoclonebackend.service.ExerciseService;
import com.testingpractice.duoclonebackend.service.JwtServiceImpl;
import com.testingpractice.duoclonebackend.service.LessonService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(pathConstants.LESSONS)
public class LessonController {

  private final LessonService lessonService;
  private final ExerciseService exerciseService;
  private final JwtServiceImpl jwtService;


  @GetMapping(pathConstants.GET_LESSONS_FROM_IDS)
  public List<LessonDto> getLessonsByIds(
          @RequestParam List<Integer> lessonIds,
          @AuthenticationPrincipal(expression = "id") Integer userId
  ) {
    return lessonService.getLessonsByIds(lessonIds, userId);
  }

  @GetMapping(pathConstants.GET_EXERCISES_BY_LESSON)
  public List<ExerciseDto> getExercisesByLessonId(
          @PathVariable Integer lessonId,
          @AuthenticationPrincipal(expression = "id") Integer userId
  ) {
    return exerciseService.getExercisesForLesson(lessonId, userId);
  }


}
