package com.testingpractice.duoclonebackend.domain.controller;

import com.testingpractice.duoclonebackend.constants.pathConstants;
import com.testingpractice.duoclonebackend.dto.LessonCompleteRequest;
import com.testingpractice.duoclonebackend.dto.LessonCompleteResponse;
import com.testingpractice.duoclonebackend.service.LessonCompletionService;
import com.testingpractice.duoclonebackend.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(pathConstants.LESSONS_COMPLETIONS)
public class LessonCompletionController {

  private final LessonCompletionService lessonCompletionService;

  @PostMapping(pathConstants.SUBMIT_COMPLETED_LESSON)
  public LessonCompleteResponse completeLesson(
      @RequestBody LessonCompleteRequest lessonCompleteRequest, @AuthenticationPrincipal(expression = "id") Integer userId) {
    return lessonCompletionService.getCompletedLesson(
        lessonCompleteRequest.lessonId(),
        userId,
        lessonCompleteRequest.courseId());
  }
}
