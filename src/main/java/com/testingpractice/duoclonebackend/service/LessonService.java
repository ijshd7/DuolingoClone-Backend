package com.testingpractice.duoclonebackend.service;

import com.testingpractice.duoclonebackend.dto.LessonCompleteResponse;
import com.testingpractice.duoclonebackend.dto.LessonDto;
import java.util.List;
import java.util.Set;

public interface LessonService {

  List<LessonDto> getLessonsByUnit(Integer unitId, Integer userId);

  List<LessonDto> getLessonsByIds(List<Integer> lessonIds, Integer userId);

  List<Integer> getLessonIdsByUnit(Integer unitId);

  Set<Integer> completedSetFor(Integer userId, List<Integer> lessonIds);
}
