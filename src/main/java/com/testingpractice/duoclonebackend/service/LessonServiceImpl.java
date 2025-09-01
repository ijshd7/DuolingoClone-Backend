package com.testingpractice.duoclonebackend.service;

import com.testingpractice.duoclonebackend.dto.LessonDto;
import com.testingpractice.duoclonebackend.entity.Lesson;
import com.testingpractice.duoclonebackend.mapper.LessonMapper;
import com.testingpractice.duoclonebackend.repository.LessonRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;
    private final LessonMapper lessonMapper;

    public LessonServiceImpl(LessonRepository lessonRepository, LessonMapper lessonMapper) {
        this.lessonRepository = lessonRepository;
        this.lessonMapper = lessonMapper;
    }

    public List<LessonDto> getLessonsByUnit (Integer unitId) {
        List<Lesson> lessons = lessonRepository.findAllByUnitId(unitId);
        return lessonMapper.toDtoList(lessons);
    }

    public List<LessonDto> getLessonsByIds (List<Integer> lessonIds) {
        List<Lesson> lessons = lessonRepository.findAllById(lessonIds);
        return lessonMapper.toDtoList(lessons);
    }

    public List<Integer> getLessonIdsByUnit (Integer unitId) {
        List<Integer> lessonIds = lessonRepository.findAllLessonIdsByUnitId(unitId);
        return lessonIds;
    }

}
