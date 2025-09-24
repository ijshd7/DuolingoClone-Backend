package com.testingpractice.duoclonebackend.service;

import com.testingpractice.duoclonebackend.dto.ExerciseAttemptResponse;
import com.testingpractice.duoclonebackend.entity.AttemptOption;
import com.testingpractice.duoclonebackend.entity.Exercise;
import com.testingpractice.duoclonebackend.entity.ExerciseAttempt;
import com.testingpractice.duoclonebackend.entity.ExerciseOption;
import com.testingpractice.duoclonebackend.repository.ExerciseAttemptOptionRepository;
import com.testingpractice.duoclonebackend.repository.ExerciseAttemptRepository;
import com.testingpractice.duoclonebackend.repository.ExerciseOptionRepository;
import com.testingpractice.duoclonebackend.repository.ExerciseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class ExerciseAttemptServiceImpl implements ExerciseAttemptService {

    private final ExerciseAttemptRepository exerciseAttemptRepository;
    private final ExerciseRepository exerciseRepository;
    private final ExerciseOptionRepository exerciseOptionRepository;
    private final ExerciseOptionService exerciseOptionService;
    private final ExerciseAttemptOptionRepository exerciseAttemptOptionRepository;

    public List<ExerciseAttempt> getLessonExerciseAttemptsForUser(Integer lessonId, Integer userId) {
        List<Exercise> lessonExercises = exerciseRepository.findAllByLessonId(lessonId);
        List<Integer> exerciseIds = lessonExercises.stream().map(Exercise::getId).toList();
        return exerciseAttemptRepository.findAllByExerciseIdInAndUserIdAndUnchecked(exerciseIds, userId);
    }

    @Transactional
    public void markAttemptsAsChecked (Integer userId, Integer lessonId) {
        exerciseAttemptRepository.markUncheckedByUserAndLesson(userId, lessonId);
    }

    @Transactional
    public ExerciseAttemptResponse submitExerciseAttempt(Integer exerciseId, ArrayList<Integer> optionIds, Integer userId) {

        List<ExerciseOption> options = exerciseOptionRepository.findAllByIdIn(optionIds);
        List<Integer> correctOptions = exerciseOptionRepository.findCorrectOptionIds(exerciseId);

        boolean areCorrect = exerciseOptionService.areOptionsCorrect(optionIds, correctOptions);

        ExerciseAttempt attempt = new ExerciseAttempt();
        attempt.setOptionId(options.getFirst().getId());
        attempt.setExerciseId(exerciseId);
        attempt.setUserId(userId);
        attempt.setSubmittedAt(new Timestamp(System.currentTimeMillis()));
        updateScore(attempt, areCorrect);
        exerciseAttemptRepository.save(attempt);

        List<AttemptOption> attemptOptions = getAttemptOptions(attempt, optionIds);
        exerciseAttemptOptionRepository.saveAll(attemptOptions);

        return getExerciseAttemptResponseMessage(optionIds, exerciseId, attempt, areCorrect);

    }

    private void updateScore(ExerciseAttempt attempt, boolean areCorrect) {
        if (areCorrect) {
            attempt.setScore(5);
        } else {
            attempt.setScore(0);
        }
    }

    private ExerciseAttemptResponse getExerciseAttemptResponseMessage (ArrayList<Integer> optionIds, Integer exerciseId, ExerciseAttempt attempt, boolean areCorrect) {

        List<Integer> correctOptions = exerciseOptionRepository.findCorrectOptionIds(exerciseId);

       ArrayList<Integer> correctResponses = exerciseOptionService.getCorrectExerciseResponses(optionIds, correctOptions);
       String correctAnswer = exerciseOptionService.getCorrectExerciseAnswerText(correctOptions);

       if (areCorrect) {
           return new ExerciseAttemptResponse(true, attempt.getScore(), "Correct!", correctResponses, correctAnswer);
       } else {
           return new ExerciseAttemptResponse(false, attempt.getScore(), "Incorrect!", correctResponses, correctAnswer);
       }

    }

    private List<AttemptOption> getAttemptOptions (ExerciseAttempt attempt, ArrayList<Integer> optionIds) {

        return IntStream.range(0, optionIds.size())
                .mapToObj(i -> {
                    AttemptOption attemptOption = new AttemptOption();
                    attemptOption.setAttemptId(attempt.getId());
                    attemptOption.setOptionId(optionIds.get(i));
                    attemptOption.setPosition(i + 1);
                    return attemptOption;
                })
                .toList();

    }

}

