package com.testingpractice.duoclonebackend.service;

import com.testingpractice.duoclonebackend.dto.LessonCompleteResponse;
import com.testingpractice.duoclonebackend.dto.NewStreakCount;
import com.testingpractice.duoclonebackend.dto.UserCourseProgressDto;
import com.testingpractice.duoclonebackend.entity.ExerciseAttempt;
import com.testingpractice.duoclonebackend.entity.Lesson;
import com.testingpractice.duoclonebackend.entity.User;
import com.testingpractice.duoclonebackend.entity.UserCourseProgress;
import com.testingpractice.duoclonebackend.enums.QuestCode;
import com.testingpractice.duoclonebackend.mapper.LessonMapper;
import com.testingpractice.duoclonebackend.mapper.UserCourseProgressMapper;
import com.testingpractice.duoclonebackend.repository.LessonCompletionRepository;
import com.testingpractice.duoclonebackend.repository.UserRepository;
import com.testingpractice.duoclonebackend.utils.AccuracyScoreUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonCompletionServiceImpl implements LessonCompletionService{

    private final LookupService lookupService;
    private final StreakService streakService;
    private final ExerciseAttemptService exerciseAttemptService;
    private final LessonCompletionRepository lessonCompletionRepository;
    private final CourseProgressService courseProgressService;
    private final UserRepository userRepository;
    private final LessonMapper lessonMapper;
    private final UserCourseProgressMapper userCourseProgressMapper;
    private final QuestService questService;
    private final UserServiceImpl userServiceImpl;

    @Transactional
    public LessonCompleteResponse getCompletedLesson (Integer lessonId, Integer userId, Integer courseId) {

        List<ExerciseAttempt> exerciseAttempts = exerciseAttemptService.getLessonExerciseAttemptsForUser(lessonId, userId);
        User user = lookupService.userOrThrow(userId);
        Lesson lesson = lookupService.lessonOrThrow(lessonId);

        Integer scoreForLesson = AccuracyScoreUtils.getLessonPoints(exerciseAttempts);
        Integer lessonAccuracy = AccuracyScoreUtils.getLessonAccuracy(exerciseAttempts);

        // -- UPDATE USERS SCORE -- //
        user.setPoints(user.getPoints() + scoreForLesson);

        // -- MARK ALL ATTEMPTS FROM THE LESSON AS CHECKED (I.E. SOFT DELETE) -- //
        exerciseAttemptService.markAttemptsAsChecked(userId, lessonId);

        // -- UPDATE USER STREAK -- //
        NewStreakCount newStreakCount = streakService.updateUserStreak(user);

        // -- UPDATE USERS NEXT LESSON -- //
        courseProgressService.updateUsersNextLesson(userId, courseId, lesson);
        UserCourseProgressDto userCourseProgressDto = userServiceImpl.getUserCourseProgress(courseId, userId);

        lessonCompletionRepository.insertIfAbsent(userId, lessonId, courseId, 15, Timestamp.from((Instant.now())));
        userRepository.save(user);

        updateQuests(userId, lessonAccuracy, newStreakCount);

        return new LessonCompleteResponse(
                scoreForLesson,
                user.getPoints(),
                lessonAccuracy,
                lessonId,
                lessonMapper.toDto(
                        lesson, lessonCompletionRepository.existsByIdUserIdAndIdLessonId(userId, lessonId)),
                userCourseProgressDto, newStreakCount,
                AccuracyScoreUtils.getAccuracyMessage(lessonAccuracy));
    }

    private Integer getCompletedLessonsInCourse (Integer userId, Integer courseId) {
        Integer completedLessonsInCourse =
                lessonCompletionRepository.countByUserAndCourse(userId, courseId);

        if (completedLessonsInCourse == null) return 0;
        else return completedLessonsInCourse;

    }

    private void updateQuests (Integer userId, Integer lessonAccuracy, NewStreakCount newStreakCount) {

        if (lessonAccuracy == 100) {
            questService.updateQuestProgress(userId, QuestCode.PERFECT);
        }

        if (lessonAccuracy > 90) {
            questService.updateQuestProgress(userId, QuestCode.ACCURACY);
        }

        Integer prev = newStreakCount.oldCount();
        Integer next = newStreakCount.newCount();

        if (next > prev) {
            questService.updateQuestProgress(userId, QuestCode.STREAK);
        }

    }


}
