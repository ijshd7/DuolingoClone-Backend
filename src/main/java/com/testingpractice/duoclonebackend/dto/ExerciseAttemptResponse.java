package com.testingpractice.duoclonebackend.dto;

import java.util.ArrayList;

public record ExerciseAttemptResponse(boolean correct, int score, String message, ArrayList<Integer> correctResponses) {}
