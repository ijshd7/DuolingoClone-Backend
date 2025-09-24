package com.testingpractice.duoclonebackend.service;

import com.testingpractice.duoclonebackend.dto.LessonCompleteResponse;

public interface LessonCompletionService {

    LessonCompleteResponse getCompletedLesson (Integer lessonId, Integer userId, Integer courseId);

}
