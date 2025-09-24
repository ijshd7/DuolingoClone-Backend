package com.testingpractice.duoclonebackend.service;

import com.testingpractice.duoclonebackend.dto.ExerciseAttemptResponse;
import com.testingpractice.duoclonebackend.dto.ExerciseDto;
import com.testingpractice.duoclonebackend.entity.AttemptOption;
import com.testingpractice.duoclonebackend.entity.Exercise;
import com.testingpractice.duoclonebackend.entity.ExerciseAttempt;
import com.testingpractice.duoclonebackend.entity.ExerciseOption;
import com.testingpractice.duoclonebackend.mapper.ExerciseAttemptMapper;
import com.testingpractice.duoclonebackend.mapper.ExerciseMapper;
import com.testingpractice.duoclonebackend.repository.ExerciseAttemptOptionRepository;
import com.testingpractice.duoclonebackend.repository.ExerciseAttemptRepository;
import com.testingpractice.duoclonebackend.repository.ExerciseOptionRepository;
import com.testingpractice.duoclonebackend.repository.ExerciseRepository;
import jakarta.transaction.Transactional;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExerciseServiceImpl implements ExerciseService {

  private final ExerciseRepository exerciseRepository;
  private final ExerciseOptionRepository exerciseOptionRepository;
  private final ExerciseAttemptRepository exerciseAttemptRepository;
  private final ExerciseAttemptOptionRepository exerciseAttemptOptionRepository;
  private final ExerciseOptionService exerciseOptionService;

  @Transactional
  public List<ExerciseDto> getExercisesForLesson(Integer lessonId, Integer userId) {

    exerciseAttemptRepository.markUncheckedByUserAndLesson(userId, lessonId);

    List<Exercise> exercises = new ArrayList<>(exerciseRepository.findAllByLessonId(lessonId));
    exercises.sort(Comparator.comparingInt(Exercise::getOrderIndex));

    List<ExerciseDto> exerciseDtosWithRandomizedOptionOrder = exerciseOptionService.getRandomizedExercisesForLesson(exercises, userId);
    return exerciseDtosWithRandomizedOptionOrder;

  }

  @Override
  @Transactional
  public ExerciseAttemptResponse submitExerciseAttempt(Integer exerciseId, ArrayList<Integer> optionIds, Integer userId) {

    List<ExerciseOption> options = exerciseOptionRepository.findAllByIdIn(optionIds);
    List<Integer> correctOptions = exerciseOptionRepository.findCorrectOptionIds(exerciseId);

    boolean areCorrect = correctOptions.equals(optionIds);

    ExerciseAttempt attempt = new ExerciseAttempt();
    attempt.setOptionId(options.getFirst().getId());
    attempt.setExerciseId(exerciseId);
    attempt.setUserId(userId);
    attempt.setSubmittedAt(new Timestamp(System.currentTimeMillis()));

    //TODO add some sort of perfect score mechanic
    //List<AttemptOption> pastAttemptsForCurrentExercise

    if (areCorrect) {
      attempt.setScore(5);
    } else {
      attempt.setScore(0);
    }

    exerciseAttemptRepository.save(attempt);

    List<AttemptOption> attemptOptions =
            IntStream.range(0, optionIds.size())
                    .mapToObj(i -> {
                      AttemptOption attemptOption = new AttemptOption();
                      attemptOption.setAttemptId(attempt.getId());
                      attemptOption.setOptionId(optionIds.get(i));
                      attemptOption.setPosition(i + 1);
                      return attemptOption;
                    })
                    .toList();

    ArrayList<Integer> correctResponses = new ArrayList<>();

    String correctAnswer = "";
    List<ExerciseOption> correctExerciseOptions = exerciseOptionRepository.findAllByIdIn(correctOptions);


    correctAnswer = parseCorrectAnswer(correctExerciseOptions);
    for (int i = 0; i < optionIds.size(); i++) {
      if (i < correctOptions.size() && correctOptions.get(i) != null && correctOptions.get(i).equals(optionIds.get(i))) {
        correctResponses.add(optionIds.get(i));
      }
    }

    exerciseAttemptOptionRepository.saveAll(attemptOptions);

    if (areCorrect) {
      return new ExerciseAttemptResponse(true, attempt.getScore(), "Correct!", correctResponses, correctAnswer);
    } else {
      return new ExerciseAttemptResponse(false, attempt.getScore(), "Incorrect!", correctResponses, correctAnswer);
    }

  }

  private String parseCorrectAnswer (List<ExerciseOption> correctOptions) {
    return correctOptions.stream()
            .sorted(Comparator.comparingInt(ExerciseOption::getAnswerOrder))
            .map(ExerciseOption::getContent)
            .collect(Collectors.joining(" "));
  }



}
