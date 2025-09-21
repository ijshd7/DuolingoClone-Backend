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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;

@Service
public class ExerciseServiceImpl implements ExerciseService {

  private final ExerciseRepository exerciseRepository;
  private final ExerciseOptionRepository exerciseOptionRepository;
  private final ExerciseMapper exerciseMapper;
  private final ExerciseAttemptRepository exerciseAttemptRepository;
  private final ExerciseAttemptMapper exerciseAttemptMapper;
  private final ExerciseAttemptOptionRepository exerciseAttemptOptionRepository;

  public ExerciseServiceImpl(
          ExerciseRepository exerciseRepository,
          ExerciseOptionRepository exerciseOptionRepository,
          ExerciseMapper exerciseMapper,
          ExerciseAttemptRepository exerciseAttemptRepository,
          ExerciseAttemptMapper exerciseAttemptMapper, ExerciseAttemptOptionRepository exerciseAttemptOptionRepository) {
    this.exerciseRepository = exerciseRepository;
    this.exerciseOptionRepository = exerciseOptionRepository;
    this.exerciseMapper = exerciseMapper;
    this.exerciseAttemptRepository = exerciseAttemptRepository;
    this.exerciseAttemptMapper = exerciseAttemptMapper;
    this.exerciseAttemptOptionRepository = exerciseAttemptOptionRepository;
  }

  @Transactional
  public List<ExerciseDto> getExercisesForLesson(Integer lessonId, Integer userId) {

    exerciseAttemptRepository.markUncheckedByUserAndLesson(userId, lessonId);

    List<Exercise> exercises = exerciseRepository.findAllByLessonId(lessonId);
    return exercises.stream()
        .map(
            exercise -> {
              List<ExerciseOption> exerciseOptions =
                  exerciseOptionRepository.findAllByExerciseId(exercise.getId());
              return exerciseMapper.toDto(exercise, exerciseOptions);
            })
        .collect(Collectors.toList());
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

    for (int i = 0; i < optionIds.size(); i++) {
      if (i < correctOptions.size() && correctOptions.get(i) != null && correctOptions.get(i).equals(optionIds.get(i))) {
        correctResponses.add(optionIds.get(i));
      }
    }

    exerciseAttemptOptionRepository.saveAll(attemptOptions);

    if (areCorrect) {
      return new ExerciseAttemptResponse(true, attempt.getScore(), "Correct!", correctResponses);
    } else {
      return new ExerciseAttemptResponse(false, attempt.getScore(), "Incorrect!", correctResponses);
    }

  }
}
