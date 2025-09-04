package com.testingpractice.duoclonebackend.domain.controller;

import com.testingpractice.duoclonebackend.constants.pathConstants;
import com.testingpractice.duoclonebackend.dto.ExerciseDto;
import com.testingpractice.duoclonebackend.dto.LessonCompleteRequest;
import com.testingpractice.duoclonebackend.dto.LessonCompleteResponse;
import com.testingpractice.duoclonebackend.dto.LessonDto;
import com.testingpractice.duoclonebackend.service.ExerciseService;
import com.testingpractice.duoclonebackend.service.LessonService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(pathConstants.LESSONS)
public class LessonController {

    private final LessonService lessonService;
    private final ExerciseService exerciseService;

    public LessonController(LessonService lessonService, ExerciseService exerciseService) {
        this.lessonService = lessonService;
        this.exerciseService = exerciseService;
    }

    @GetMapping(pathConstants.LESSONS_FROM_IDS)
    public List<LessonDto> getLessonsByIds (@RequestParam List<Integer> lessonIds) {
       return lessonService.getLessonsByIds(lessonIds);
    }

    @GetMapping(pathConstants.LESSON_EXERCISES)
    public List<ExerciseDto> getExercisesByLessonId (@PathVariable Integer lessonId, @PathVariable Integer userId) {

        return exerciseService.getExercisesForLesson(lessonId, userId);
    }

    @PostMapping("/completedLesson")
    public LessonCompleteResponse completeLesson (
            @RequestBody LessonCompleteRequest lessonCompleteRequest
            ) {

        return lessonService.getCompletedLesson(lessonCompleteRequest.lessonId(), lessonCompleteRequest.userId(), lessonCompleteRequest.courseId());

    }



}
