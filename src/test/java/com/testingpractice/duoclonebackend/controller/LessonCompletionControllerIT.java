package com.testingpractice.duoclonebackend.controller;

import static com.testingpractice.duoclonebackend.testutils.TestConstants.*;
import static com.testingpractice.duoclonebackend.testutils.TestUtils.*;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import com.testingpractice.duoclonebackend.constants.pathConstants;
import com.testingpractice.duoclonebackend.dto.LessonCompleteResponse;
import com.testingpractice.duoclonebackend.entity.Exercise;
import com.testingpractice.duoclonebackend.entity.ExerciseAttempt;
import com.testingpractice.duoclonebackend.entity.Lesson;
import com.testingpractice.duoclonebackend.entity.User;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class LessonCompletionControllerIT extends AbstractIntegrationTest {

    private Lesson l1;
    private Lesson l2;
    private Lesson l3;
  private Lesson previousLesson;
  private Lesson completedLesson;
  private Lesson nextLesson;

  private Exercise e1;
  private Exercise e2;
  private Exercise e3;


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

    l1 = savedLessons.get(0);
    l2 = savedLessons.get(1);
    l3 = savedLessons.get(2);

    previousLesson = savedLessons.get(3);
    completedLesson = savedLessons.get(4);
    nextLesson = savedLessons.get(5);
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

    e1 = savedExercises.get(0);
    e2 = savedExercises.get(1);
    e3 = savedExercises.get(2);




  }

  private void setupUserCompletionForTest (Integer correctScores, Integer usersPoints, Integer streakLength, Timestamp lastSubmission, Timestamp AttemptTime) {

    User user = userRepository.save(makeUser(1, "testuser", "test", "user", "emailOne", "default", usersPoints, FIXED_TIMESTAMP_1, lastSubmission, streakLength));
    Integer userId = user.getId();

    userCourseProgressRepository.save(makeUserCourseProgress(userId, 1, false, previousLesson.getId(), FIXED_TIMESTAMP_1));

    List<Integer> exercises = List.of(
            e1.getId(),
            e2.getId(),
            e3.getId()
    );

    for (int i = 0; i < 3; i++) {
      Integer score = i < correctScores ? 5 : 0;
      exerciseAttemptRepository.save(makeExerciseAttempt(exercises.get(i), userId, false, AttemptTime, i + 1, score));
    }

    lessonCompletionRepository.saveAll(
            List.of(
                    makeLessonCompletion(userId, l1.getId(), 1, 15),
                    makeLessonCompletion(userId, l2.getId(), 1, 20),
                    makeLessonCompletion(userId, l3.getId(), 1, 10),
                    makeLessonCompletion(userId, previousLesson.getId(), 1, 10)));
  }

  @Test
  void submitLesson_happyPath_returnsCorrectResponse () {

    setupUserCompletionForTest(3, 0, 1, FIXED_TIMESTAMP_1, FIXED_TIMESTAMP_2);

    Integer userId = 1;
    Integer lessonId = completedLesson.getId();
    Integer nextLessonId = nextLesson.getId();

    LessonCompleteResponse response =
        given()
            .contentType("application/json")
            .body(
                Map.of(
                    "lessonId", lessonId,
                    "courseId", 1))
            .when()
            .post(pathConstants.LESSONS_COMPLETIONS + pathConstants.SUBMIT_COMPLETED_LESSON)
            .then()
            .statusCode(200)
            .extract()
            .as(LessonCompleteResponse.class);

    assertThat(response.userId()).isEqualTo(userId);
    assertThat(response.lessonId()).isEqualTo(lessonId);
    assertThat(response.newUserScore()).isGreaterThan(15);
    assertThat(response.totalScore()).isGreaterThan(15);
    assertThat(response.accuracy()).isEqualTo(100);
    assertThat(response.message()).isNotNull();
    assertThat(response.updatedUserCourseProgress().currentLessonId()).isEqualTo(nextLessonId);

  }





}
