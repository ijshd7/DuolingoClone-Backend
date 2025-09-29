package com.testingpractice.duoclonebackend.domain.controller;

import com.testingpractice.duoclonebackend.service.JwtServiceImpl;
import com.testingpractice.duoclonebackend.constants.pathConstants;
import com.testingpractice.duoclonebackend.dto.ExerciseDto;
import com.testingpractice.duoclonebackend.dto.LessonDto;
import com.testingpractice.duoclonebackend.service.ExerciseService;
import com.testingpractice.duoclonebackend.service.LessonService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
          @CookieValue(name = "jwt", required = false) String token) {

    int userId = jwtService.requireUserId(token);
    // Delegate to your service
    return lessonService.getLessonsByIds(lessonIds, userId);
  }

  @GetMapping(pathConstants.GET_EXERCISES_BY_LESSON)
  public List<ExerciseDto> getExercisesByLessonId(
      @PathVariable Integer lessonId, @PathVariable Integer userId) {
    return exerciseService.getExercisesForLesson(lessonId, userId);
  }


}
