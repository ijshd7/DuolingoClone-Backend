package com.testingpractice.duoclonebackend.dto;

import java.util.ArrayList;

public record ExerciseAttemptRequest(
    Integer exerciseId, ArrayList<Integer> optionIds, Integer userId) {}
