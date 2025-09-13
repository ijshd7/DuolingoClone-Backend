package com.testingpractice.duoclonebackend.domain.controller;

import com.testingpractice.duoclonebackend.constants.pathConstants;
import com.testingpractice.duoclonebackend.dto.ExerciseDto;
import com.testingpractice.duoclonebackend.dto.LessonCompleteRequest;
import com.testingpractice.duoclonebackend.dto.LessonCompleteResponse;
import com.testingpractice.duoclonebackend.dto.LessonDto;
import com.testingpractice.duoclonebackend.service.ExerciseService;
import com.testingpractice.duoclonebackend.service.LessonService;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(pathConstants.LESSONS)
public class LessonController {

  private final LessonService lessonService;
  private final ExerciseService exerciseService;

  public LessonController(LessonService lessonService, ExerciseService exerciseService) {
    this.lessonService = lessonService;
    this.exerciseService = exerciseService;
  }

  @GetMapping(pathConstants.GET_LESSONS_FROM_IDS)
  public List<LessonDto> getLessonsByIds(
      @RequestParam List<Integer> lessonIds, @RequestParam Integer userId) {
    return lessonService.getLessonsByIds(lessonIds, userId);
  }

  @GetMapping(pathConstants.GET_EXERCISES_BY_LESSON)
  public List<ExerciseDto> getExercisesByLessonId(
      @PathVariable Integer lessonId, @PathVariable Integer userId) {
    return exerciseService.getExercisesForLesson(lessonId, userId);
  }

  @PostMapping(pathConstants.SUBMIT_COMPLETED_LESSON)
  public LessonCompleteResponse completeLesson(
      @RequestBody LessonCompleteRequest lessonCompleteRequest) {



    return lessonService.getCompletedLesson(
        lessonCompleteRequest.lessonId(),
        lessonCompleteRequest.userId(),
        lessonCompleteRequest.courseId());
  }
}
