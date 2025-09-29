package com.testingpractice.duoclonebackend.utils;

import com.testingpractice.duoclonebackend.entity.ExerciseAttempt;
import java.util.List;

public class AccuracyScoreUtils {

  public static String getAccuracyMessage(int accuracy) {
    if (accuracy <= 30) {
      return "OK";
    } else if (accuracy <= 60) {
      return "GOOD";
    } else if (accuracy <= 80) {
      return "GREAT";
    } else {
      return "AMAZING";
    }
  }

  public static Integer getLessonAccuracy(List<ExerciseAttempt> exerciseAttempts) {
    int earned = exerciseAttempts.stream().mapToInt(ExerciseAttempt::getScore).sum();

    int max = exerciseAttempts.size() * 5;
    int accuracyPercent = (max == 0) ? 0 : (int) ((double) earned / max * 100);

    return accuracyPercent;
  }

  public static Integer getLessonPoints(
      List<ExerciseAttempt> exerciseAttempts, boolean isFirstAttempt, Integer accuracy) {

    boolean isPerfect = accuracy >= 100;

    int basePoints = 0;

    if (isPerfect) {
      basePoints += 5;
    }

    if (isFirstAttempt) {
      basePoints += exerciseAttempts.stream().mapToInt(ExerciseAttempt::getScore).sum();
    } else {
      basePoints += 5;
    }

    return basePoints;
  }
}
