package com.testingpractice.duoclonebackend.service;

import com.testingpractice.duoclonebackend.dto.LessonCompleteResponse;
import com.testingpractice.duoclonebackend.dto.LessonDto;
import com.testingpractice.duoclonebackend.entity.*;
import com.testingpractice.duoclonebackend.exception.ApiException;
import com.testingpractice.duoclonebackend.exception.ErrorCode;
import com.testingpractice.duoclonebackend.mapper.LessonMapper;
import com.testingpractice.duoclonebackend.mapper.UserCourseProgressMapper;
import com.testingpractice.duoclonebackend.repository.*;
import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
    private final LessonCompletionRepository lessonCompletionRepository;
    private final UserCourseProgressMapper userCourseProgressMapper;

    public LessonServiceImpl(LessonRepository lessonRepository, LessonMapper lessonMapper, UserCourseProgressRepository userCourseProgressRepository, UserRepository userRepository, ExerciseAttemptRepository exerciseAttemptRepository, UnitRepository unitRepository, SectionRepository sectionRepository, ExerciseRepository exerciseRepository, LessonCompletionRepository lessonCompletionRepository, UserCourseProgressMapper userCourseProgressMapper) {
        this.lessonRepository = lessonRepository;
        this.lessonMapper = lessonMapper;
        this.userCourseProgressRepository = userCourseProgressRepository;
        this.userRepository = userRepository;
        this.exerciseAttemptRepository = exerciseAttemptRepository;
        this.unitRepository = unitRepository;
        this.sectionRepository = sectionRepository;
        this.exerciseRepository = exerciseRepository;
        this.lessonCompletionRepository = lessonCompletionRepository;
        this.userCourseProgressMapper = userCourseProgressMapper;
    }

    public List<LessonDto> getLessonsByUnit (Integer unitId, Integer userId) {
        List<Lesson> lessons = lessonRepository.findAllByUnitId(unitId);
        return lessonMapper.toDtoList(lessons, completedSetFor(userId, lessons.stream().map(Lesson::getId).toList()));
    }

    public List<LessonDto> getLessonsByIds (List<Integer> lessonIds, Integer userId) {
        List<Lesson> lessons = lessonRepository.findAllById(lessonIds);
        return lessonMapper.toDtoList(lessons, completedSetFor(userId, lessons.stream().map(Lesson::getId).toList()));
    }

    public List<Integer> getLessonIdsByUnit (Integer unitId) {
        List<Integer> lessonIds = lessonRepository.findAllLessonIdsByUnitId(unitId);
        return lessonIds;
    }

    public Set<Integer> completedSetFor(Integer userId, List<Integer> lessonIds) {
        if (lessonIds.isEmpty()) return Set.of();
        return new HashSet<>(lessonCompletionRepository.findCompletedLessonIdsIn(userId, lessonIds));
    }

    @Transactional
    public LessonCompleteResponse getCompletedLesson (Integer lessonId, Integer userId, Integer courseId) {

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

        if (userCourseProgress.getCurrentLessonId().equals(lessonId)) {



            //UPDATE USERS CURRENT LESSON
            Lesson nextLesson = getNextLesson(lesson, userId, courseId);
            if (nextLesson == null) throw new ApiException(ErrorCode.LESSON_NOT_FOUND, HttpStatus.NOT_FOUND);
            userCourseProgress.setCurrentLessonId(nextLesson.getId());
        }

        boolean hasAlreadyCompletedLesson = lessonCompletionRepository.existsByIdUserIdAndIdLessonId(userId, lessonId);

        if (!hasAlreadyCompletedLesson) {
            LessonCompletion lessonCompletion = new LessonCompletion();
            lessonCompletion.setId(new LessonCompletionId(userId, lessonId));
            lessonCompletion.setCourseId(courseId);
            lessonCompletion.setScore(15);
            lessonCompletion.setCompletedAt(Timestamp.from(Instant.now()));

            lessonCompletionRepository.save(lessonCompletion);
        }

        userRepository.save(user);

        userCourseProgressRepository.save(userCourseProgress);


        LessonCompleteResponse response = new LessonCompleteResponse(scoreForLesson, lessonId,
                lessonMapper.toDto(lesson, lessonCompletionRepository.existsByIdUserIdAndIdLessonId(userId, lessonId)),
                userCourseProgressMapper.toDto(userCourseProgress),
                "Lesson Complete!"
                );


        return response;

    }

    private Integer getScoreForLesson (Integer lessonId, Integer userId) {
        List<Exercise> lessonExercises = exerciseRepository.findAllByLessonId(lessonId);

        if (lessonExercises.isEmpty()) return 0;

        List<Integer> exerciseIds = lessonExercises.stream()
                .map(Exercise::getId)
                .toList();

        List<ExerciseAttempt> exerciseAttempts =
                exerciseAttemptRepository.findAllByExerciseIdInAndUserIdAndUnchecked(exerciseIds, userId);

        exerciseAttemptRepository.markUncheckedByUserAndLesson(userId, lessonId);

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
