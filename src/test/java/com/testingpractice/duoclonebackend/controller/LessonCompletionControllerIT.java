package com.testingpractice.duoclonebackend.controller;

import static com.testingpractice.duoclonebackend.testutils.TestConstants.*;
import static com.testingpractice.duoclonebackend.testutils.TestUtils.*;

import com.testingpractice.duoclonebackend.entity.Exercise;
import com.testingpractice.duoclonebackend.entity.Lesson;
import com.testingpractice.duoclonebackend.entity.User;
import com.testingpractice.duoclonebackend.enums.QuestCode;
import com.testingpractice.duoclonebackend.repository.*;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class LessonCompletionControllerIT extends AbstractIntegrationTest {

  private Lesson completedLesson;


  @BeforeEach
  void seed() {

    sectionRepository.save(makeSection("Section 1", 1, 1));

    unitRepository.saveAll(
        List.of(
            makeUnit("Unit 1", 1, 1, 1), makeUnit("Unit 2", 1, 1, 2), makeUnit("Unit 3", 1, 1, 3)));



    List<Lesson> savedLessons =
        lessonRepository.saveAll(
            List.of(
                makeLesson(LESSON_1_TITLE, 1, 1, "Exercise"),
                makeLesson(LESSON_2_TITLE, 1, 2, "Exercise"),
                makeLesson(LESSON_3_TITLE, 2, 1, "Exercise"),
                makeLesson(LESSON_4_TITLE, 2, 2, "Exercise"),
                makeLesson(LESSON_5_TITLE, 3, 1, "Exercise"),
                makeLesson(LESSON_6_TITLE, 3, 2, "Exercise")));

    Lesson l1 = savedLessons.get(0);
    Lesson l2 = savedLessons.get(1);
    Lesson l3 = savedLessons.get(2);
    Lesson l4 = savedLessons.get(3);
    completedLesson = savedLessons.get(4);
    Integer completedLessonId = completedLesson.getId();

    questDefinitionRepository.saveAll(List.of(
            makeQuestDefition("STREAK", 1, 10, true),
            makeQuestDefition("ACCURACY", 2, 10, true),
            makeQuestDefition("PERFECT", 1, 10, true)
    ));

    monthlyChallengeDefinitionRepository.save(makeMonthlyChallengeDefinition("COMPLETE_QUESTS", 30, 200, true));

    List<Exercise> savedExercises = exerciseRepository.saveAll(
            List.of(
                    makeExercise(completedLessonId, "Translate", 1),
                    makeExercise(completedLessonId, "Translate", 1),
                    makeExercise(completedLessonId, "Translate", 1)
            )
    );

    Exercise e1 = savedExercises.get(0);
    Exercise e2 = savedExercises.get(1);
    Exercise e3 = savedExercises.get(2);


    User user = userRepository.save(makeUser(1, "testuser", "test", "user", "emailOne", "default", 0, FIXED_TIMESTAMP_1, FIXED_TIMESTAMP_1, 1));
    Integer userId = user.getId();

    userCourseProgressRepository.save(makeUserCourseProgress(userId, 1, false, l4.getId()));

    exerciseAttemptRepository.saveAll(
        List.of(
            makeExerciseAttempt(e1.getId(), userId, false, FIXED_TIMESTAMP_2, 1, 0),
            makeExerciseAttempt(e2.getId(), userId, false, FIXED_TIMESTAMP_2, 2, 5),
            makeExerciseAttempt(e3.getId(), userId, false, FIXED_TIMESTAMP_2, 3, 5)));

    lessonCompletionRepository.saveAll(
        List.of(
            makeLessonCompletion(userId, l1.getId(), 1, 15),
            makeLessonCompletion(userId, l2.getId(), 1, 20),
            makeLessonCompletion(userId, l3.getId(), 1, 10),
            makeLessonCompletion(userId, l4.getId(), 1, 10)));
  }
}
