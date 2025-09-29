package com.testingpractice.duoclonebackend.service;

import com.testingpractice.duoclonebackend.dto.ExerciseDto;
import com.testingpractice.duoclonebackend.entity.Exercise;

import java.util.ArrayList;
import java.util.List;

public interface ExerciseOptionService {

    List<ExerciseDto> getRandomizedExercisesForLesson (List<Exercise> exercises, Integer userId);

    ArrayList<Integer> getCorrectExerciseResponses(ArrayList<Integer> optionIds, List<Integer> correctOptions);

    String getCorrectExerciseAnswerText (List<Integer> correctOptions);

    boolean areOptionsCorrect (ArrayList<Integer> optionIds, List<Integer> correctOptions);

}
