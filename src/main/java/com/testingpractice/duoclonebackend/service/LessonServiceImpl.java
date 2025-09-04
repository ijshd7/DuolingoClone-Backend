package com.testingpractice.duoclonebackend.service;

import com.testingpractice.duoclonebackend.dto.LessonCompleteResponse;
import com.testingpractice.duoclonebackend.dto.LessonDto;
import com.testingpractice.duoclonebackend.entity.*;
import com.testingpractice.duoclonebackend.exception.ApiException;
import com.testingpractice.duoclonebackend.exception.ErrorCode;
import com.testingpractice.duoclonebackend.mapper.LessonMapper;
import com.testingpractice.duoclonebackend.repository.*;
import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;
    private final LessonMapper lessonMapper;
    private final UserCourseProgressRepository userCourseProgressRepository;
    private final UserRepository userRepository;
    private final ExerciseAttemptRepository exerciseAttemptRepository;
    private final UnitRepository unitRepository;
    private final SectionRepository sectionRepository;
    private final ExerciseRepository exerciseRepository;

    public LessonServiceImpl(LessonRepository lessonRepository, LessonMapper lessonMapper, UserCourseProgressRepository userCourseProgressRepository, UserRepository userRepository, ExerciseAttemptRepository exerciseAttemptRepository, UnitRepository unitRepository, SectionRepository sectionRepository, ExerciseRepository exerciseRepository) {
        this.lessonRepository = lessonRepository;
        this.lessonMapper = lessonMapper;
        this.userCourseProgressRepository = userCourseProgressRepository;
        this.userRepository = userRepository;
        this.exerciseAttemptRepository = exerciseAttemptRepository;
        this.unitRepository = unitRepository;
        this.sectionRepository = sectionRepository;
        this.exerciseRepository = exerciseRepository;
    }

    public List<LessonDto> getLessonsByUnit (Integer unitId) {
        List<Lesson> lessons = lessonRepository.findAllByUnitId(unitId);
        return lessonMapper.toDtoList(lessons);
    }

    public List<LessonDto> getLessonsByIds (List<Integer> lessonIds) {
        List<Lesson> lessons = lessonRepository.findAllById(lessonIds);
        return lessonMapper.toDtoList(lessons);
    }

    public List<Integer> getLessonIdsByUnit (Integer unitId) {
        List<Integer> lessonIds = lessonRepository.findAllLessonIdsByUnitId(unitId);
        return lessonIds;
    }

    @Transactional
    public LessonCompleteResponse getCompletedLesson (Integer lessonId, Integer userId, Integer courseId) {

        System.out.println("completeLesson start lessonId={} userId={} courseId={}" + lessonId + " " + userId + " " +  courseId);


        UserCourseProgress userCourseProgress = userCourseProgressRepository.findByUserIdAndCourseId(userId, courseId);
        if (userCourseProgress == null) throw new ApiException(ErrorCode.PROGRESS_NOT_FOUND, HttpStatus.NOT_FOUND);

        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) throw new ApiException(ErrorCode.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        User user = optionalUser.get();

        //UPDATE POINTS
        Integer scoreForLesson = getScoreForLesson(lessonId, userId);
        user.setPoints(user.getPoints() + scoreForLesson);

        Optional<Lesson> optionalLesson = lessonRepository.findById(lessonId);
        if (optionalLesson.isEmpty()) throw new ApiException(ErrorCode.LESSON_NOT_FOUND, HttpStatus.NOT_FOUND);
        Lesson lesson = optionalLesson.get();

        //UPDATE USERS CURRENT LESSON
        Lesson nextLesson = getNextLesson(lesson, userId, courseId);
        if (nextLesson == null) throw new ApiException(ErrorCode.LESSON_NOT_FOUND, HttpStatus.NOT_FOUND);
        userCourseProgress.setCurrentLessonId(nextLesson.getId());

        LessonCompleteResponse response = new LessonCompleteResponse(scoreForLesson, lessonId, "COURSE COMPLETE");

        userRepository.save(user);
        userCourseProgressRepository.save(userCourseProgress);


        return response;

    }

    private Integer getScoreForLesson (Integer lessonId, Integer userId) {
        List<Exercise> lessonExercises = exerciseRepository.findAllByLessonId(lessonId);

        if (lessonExercises.isEmpty()) return 0;

        List<Integer> exerciseIds = lessonExercises.stream()
                .map(Exercise::getId)
                .toList();

        List<ExerciseAttempt> exerciseAttempts =
                exerciseAttemptRepository.findAllByExerciseIdInAndUserId(exerciseIds, userId);

        if (exerciseAttempts.isEmpty()) {
            return 0;
        }

        return exerciseAttempts.stream()
                .mapToInt(ExerciseAttempt::getScore)
                .sum();
    }

    @Nullable
    private Lesson getNextLesson (Lesson lesson, Integer userId, Integer courseId) {

        Lesson nextLessonInUnit = lessonRepository.findFirstByUnitIdAndOrderIndexGreaterThanOrderByOrderIndexAsc(lesson.getUnitId(), lesson.getOrderIndex());
        if (nextLessonInUnit != null) return nextLessonInUnit;

        Optional<Unit> currentUnit = unitRepository.findById(lesson.getUnitId());
        if (currentUnit.isEmpty()) throw new ApiException(ErrorCode.UNIT_NOT_FOUND, HttpStatus.NOT_FOUND);

        Unit nextUnit = unitRepository.findFirstBySectionIdAndOrderIndexGreaterThanOrderByOrderIndexAsc(currentUnit.get().getSectionId(), currentUnit.get().getOrderIndex());
        if (nextUnit != null) {
            return lessonRepository.findFirstByUnitIdOrderByOrderIndexAsc(nextUnit.getId());
        }

        Optional<Section> currentSection = sectionRepository.findById(currentUnit.get().getSectionId());
        if (currentSection.isEmpty()) throw new ApiException(ErrorCode.SECTION_NOT_FOUND, HttpStatus.NOT_FOUND);

        Section nextSection = sectionRepository.findFirstByCourseIdAndOrderIndexGreaterThanOrderByOrderIndexAsc(currentSection.get().getCourseId(), currentSection.get().getOrderIndex());
        if (nextSection != null) {
            Unit firstUnitOfSection = unitRepository.findFirstBySectionIdOrderByOrderIndexAsc(nextSection.getId());
            if (firstUnitOfSection == null) throw new ApiException(ErrorCode.COURSE_END, HttpStatus.NOT_FOUND);;
            return lessonRepository.findFirstByUnitIdOrderByOrderIndexAsc(firstUnitOfSection.getId());
        }

        return null;
    }



}
