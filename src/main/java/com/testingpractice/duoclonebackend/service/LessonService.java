package com.testingpractice.duoclonebackend.service;

import com.testingpractice.duoclonebackend.dto.LessonDto;
import com.testingpractice.duoclonebackend.entity.Lesson;

import java.util.List;

public interface LessonService {

    List<LessonDto> getLessonsByUnit (Integer lessonId);
    List<LessonDto> getLessonsByIds (List<Integer> lessonIds);
    List<Integer> getLessonIdsByUnit (Integer unitId);

}
