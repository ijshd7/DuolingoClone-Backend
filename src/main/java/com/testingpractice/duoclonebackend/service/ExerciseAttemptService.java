package com.testingpractice.duoclonebackend.service;

import com.testingpractice.duoclonebackend.entity.Exercise;
import com.testingpractice.duoclonebackend.entity.ExerciseAttempt;
import com.testingpractice.duoclonebackend.repository.ExerciseAttemptRepository;
import com.testingpractice.duoclonebackend.repository.ExerciseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExerciseAttemptService {

    private final ExerciseAttemptRepository exerciseAttemptRepository;
    private final ExerciseRepository exerciseRepository;

    public List<ExerciseAttempt> getLessonExerciseAttemptsForUser(Integer lessonId, Integer userId) {
        List<Exercise> lessonExercises = exerciseRepository.findAllByLessonId(lessonId);
        List<Integer> exerciseIds = lessonExercises.stream().map(Exercise::getId).toList();
        return exerciseAttemptRepository.findAllByExerciseIdInAndUserIdAndUnchecked(exerciseIds, userId);
    }

    @Transactional
    public void markAttemptsAsChecked (Integer userId, Integer lessonId) {
        exerciseAttemptRepository.markUncheckedByUserAndLesson(userId, lessonId);
    }

}
