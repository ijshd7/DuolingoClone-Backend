package com.testingpractice.duoclonebackend.service;

import com.testingpractice.duoclonebackend.dto.ExerciseDto;
import com.testingpractice.duoclonebackend.entity.Exercise;
import com.testingpractice.duoclonebackend.entity.ExerciseOption;
import com.testingpractice.duoclonebackend.mapper.ExerciseMapper;
import com.testingpractice.duoclonebackend.repository.ExerciseOptionRepository;
import com.testingpractice.duoclonebackend.repository.ExerciseRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExerciseServiceImpl implements ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final ExerciseOptionRepository exerciseOptionRepository;
    private final ExerciseMapper exerciseMapper;

    public ExerciseServiceImpl(ExerciseRepository exerciseRepository, ExerciseOptionRepository exerciseOptionRepository, ExerciseMapper exerciseMapper) {
        this.exerciseRepository = exerciseRepository;
        this.exerciseOptionRepository = exerciseOptionRepository;
        this.exerciseMapper = exerciseMapper;
    }

    public List<ExerciseDto> getExercisesForLesson (Integer lessonId) {
        List<Exercise> exercises = exerciseRepository.findAllByLessonId(lessonId);
        return exercises.stream().map(exercise -> {
                    List<ExerciseOption> exerciseOptions = exerciseOptionRepository.findAllByExerciseId(exercise.getId());
                    return exerciseMapper.toDto(exercise, exerciseOptions);
                })
                .collect(Collectors.toList());
    }


}
