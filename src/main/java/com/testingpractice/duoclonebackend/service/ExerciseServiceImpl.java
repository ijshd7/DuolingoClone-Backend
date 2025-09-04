package com.testingpractice.duoclonebackend.service;

import com.testingpractice.duoclonebackend.dto.ExerciseAttemptDto;
import com.testingpractice.duoclonebackend.dto.ExerciseAttemptResponse;
import com.testingpractice.duoclonebackend.dto.ExerciseDto;
import com.testingpractice.duoclonebackend.entity.Exercise;
import com.testingpractice.duoclonebackend.entity.ExerciseAttempt;
import com.testingpractice.duoclonebackend.entity.ExerciseOption;
import com.testingpractice.duoclonebackend.mapper.ExerciseAttemptMapper;
import com.testingpractice.duoclonebackend.mapper.ExerciseMapper;
import com.testingpractice.duoclonebackend.repository.ExerciseAttemptRepository;
import com.testingpractice.duoclonebackend.repository.ExerciseOptionRepository;
import com.testingpractice.duoclonebackend.repository.ExerciseRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExerciseServiceImpl implements ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final ExerciseOptionRepository exerciseOptionRepository;
    private final ExerciseMapper exerciseMapper;
    private final ExerciseAttemptRepository exerciseAttemptRepository;
    private final ExerciseAttemptMapper exerciseAttemptMapper;

    public ExerciseServiceImpl(ExerciseRepository exerciseRepository, ExerciseOptionRepository exerciseOptionRepository, ExerciseMapper exerciseMapper, ExerciseAttemptRepository exerciseAttemptRepository, ExerciseAttemptMapper exerciseAttemptMapper) {
        this.exerciseRepository = exerciseRepository;
        this.exerciseOptionRepository = exerciseOptionRepository;
        this.exerciseMapper = exerciseMapper;
        this.exerciseAttemptRepository = exerciseAttemptRepository;
        this.exerciseAttemptMapper = exerciseAttemptMapper;
    }

    //TODO add user id
    @Transactional
    public List<ExerciseDto> getExercisesForLesson (Integer lessonId, Integer userId) {



        exerciseAttemptRepository.markUncheckedByUserAndLesson(userId, lessonId);

        List<Exercise> exercises = exerciseRepository.findAllByLessonId(lessonId);
        return exercises.stream().map(exercise -> {
                    List<ExerciseOption> exerciseOptions = exerciseOptionRepository.findAllByExerciseId(exercise.getId());
                    return exerciseMapper.toDto(exercise, exerciseOptions);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ExerciseAttemptResponse submitExerciseAttempt(Integer exerciseId, Integer optionId, Integer userId) {

        //Check if user allowed to submit exercise

        //Check if option correct
        Optional<ExerciseOption> option = exerciseOptionRepository.findById(optionId);

        //TODO add error
        if (option.isEmpty()) return null;

        ExerciseOption optionObject = option.get();

        ExerciseAttempt attempt = new ExerciseAttempt();
        attempt.setOptionId(optionId);
        attempt.setExerciseId(exerciseId);
        attempt.setScore(0);
        attempt.setUserId(userId);
        attempt.setSubmittedAt(new Timestamp(System.currentTimeMillis()));


        if (optionObject.getIsCorrect()) {
            attempt.setScore(5);
            exerciseAttemptRepository.save(attempt);
            return new ExerciseAttemptResponse(true, attempt.getScore(), "Correct!");
        } else {
            attempt.setScore(0);
            exerciseAttemptRepository.save(attempt);
            return new ExerciseAttemptResponse(false, attempt.getScore(), "Inorrect!");
        }




    }


}
