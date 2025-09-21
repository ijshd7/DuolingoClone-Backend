package com.testingpractice.duoclonebackend.dto;

import java.lang.reflect.Array;
import java.util.ArrayList;

public record ExerciseAttemptRequest(Integer exerciseId, ArrayList<Integer> optionIds, Integer userId) {}
