package com.testingpractice.duoclonebackend.service;

import com.testingpractice.duoclonebackend.dto.ExerciseDto;
import com.testingpractice.duoclonebackend.entity.Exercise;
import com.testingpractice.duoclonebackend.entity.ExerciseOption;
import com.testingpractice.duoclonebackend.exception.ApiException;
import com.testingpractice.duoclonebackend.exception.ErrorCode;
import com.testingpractice.duoclonebackend.mapper.ExerciseMapper;
import com.testingpractice.duoclonebackend.repository.ExerciseOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExerciseOptionService {

    private final ExerciseOptionRepository exerciseOptionRepository;
    private final ExerciseMapper exerciseMapper;
    public List<ExerciseDto> getRandomizedExercisesForLesson (List<Exercise> exercises, Integer userId) {

        if (exercises.isEmpty()) throw new ApiException(ErrorCode.EXERCISES_NOT_FOUND);
        Integer lessonId = exercises.get(0).getLessonId();

        long seed = Objects.hash(lessonId, userId, LocalDate.now());
        Random rnd = new Random(seed);

        return exercises.stream()
                .map(ex -> {
                    List<ExerciseOption> opts =
                            new ArrayList<>(exerciseOptionRepository.findAllByExerciseId(ex.getId()));
                    Collections.shuffle(opts, rnd);
                    return exerciseMapper.toDto(ex, opts);
                })
                .collect(Collectors.toList());

    }

}
