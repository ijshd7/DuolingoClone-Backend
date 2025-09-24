package com.testingpractice.duoclonebackend.service;

import com.testingpractice.duoclonebackend.dto.ExerciseAttemptResponse;

import java.util.ArrayList;

public interface ExerciseAttemptService {

    ExerciseAttemptResponse submitExerciseAttempt(
            Integer exerciseId, ArrayList<Integer> optionIds, Integer userId);
}
