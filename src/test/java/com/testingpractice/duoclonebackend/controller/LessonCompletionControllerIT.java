package com.testingpractice.duoclonebackend.controller;

import static com.testingpractice.duoclonebackend.testutils.TestConstants.*;
import static com.testingpractice.duoclonebackend.testutils.TestUtils.*;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

import com.testingpractice.duoclonebackend.constants.pathConstants;
import com.testingpractice.duoclonebackend.dto.LessonCompleteResponse;
import com.testingpractice.duoclonebackend.entity.*;

import java.sql.Timestamp;
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
  private Lesson currentCourseLesson;
  private Lesson nextLesson;

  private Course course1;

  private Exercise e1;
  private Exercise e2;
  private Exercise e3;


  @BeforeEach
  void seed() {

    course1 = courseRepository.save(makeCourse("Course 1", "defaultImg"));

    Section s1 = sectionRepository.save(makeSection("Section 1", course1.getId(), 1));
    Integer s1Id = s1.getId();

    List<Unit> units = unitRepository.saveAll(
        List.of(
            makeUnit("Unit 1", course1.getId(), s1Id, 1), makeUnit("Unit 2", course1.getId(), s1Id, 2), makeUnit("Unit 3", course1.getId(), s1Id, 3)));

    Integer u1Id = units.get(0).getId();
    Integer u2Id = units.get(1).getId();
    Integer u3Id = units.get(2).getId();


    List<Lesson> savedLessons =
        lessonRepository.saveAll(
            List.of(
                makeLesson(LESSON_1_TITLE, u1Id, 1, "Exercise"),
                makeLesson(LESSON_2_TITLE, u1Id, 2, "Exercise"),
                makeLesson(LESSON_3_TITLE, u2Id, 1, "Exercise"),
                makeLesson(LESSON_4_TITLE, u2Id, 2, "Exercise"),
                makeLesson(LESSON_5_TITLE, u3Id, 1, "Exercise"),
                makeLesson(LESSON_6_TITLE, u3Id, 2, "Exercise")));

    l1 = savedLessons.get(0);
    l2 = savedLessons.get(1);
    l3 = savedLessons.get(2);

    previousLesson = savedLessons.get(3);
    currentCourseLesson = savedLessons.get(4);
    nextLesson = savedLessons.get(5);
    Integer completedLessonId = currentCourseLesson.getId();

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

  private Integer setupUserCompletionForTest (Integer correctScores, Integer usersPoints, Integer streakLength, Timestamp lastSubmission, Timestamp AttemptTime) {

    User user = userRepository.save(makeUser(course1.getId(), "testuser", "test", "user", "emailOne", "default", usersPoints, FIXED_TIMESTAMP_1, lastSubmission, streakLength));
    Integer userId = user.getId();

    userCourseProgressRepository.save(makeUserCourseProgress(userId, course1.getId(), false, currentCourseLesson.getId(), FIXED_TIMESTAMP_1));

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
                    makeLessonCompletion(userId, l1.getId(), course1.getId(), 15),
                    makeLessonCompletion(userId, l2.getId(), course1.getId(), 20),
                    makeLessonCompletion(userId, l3.getId(), course1.getId(), 10),
                    makeLessonCompletion(userId, previousLesson.getId(), course1.getId(), 10)));

    return userId;

  }

  @Test
  void submitLesson_happyPath_returnsCorrectResponse_noUpdatedLessonId () {
    Integer userId = setupUserCompletionForTest(0, 0, 1, FIXED_TIMESTAMP_1, FIXED_TIMESTAMP_2);
    Integer lessonId = previousLesson.getId();
    Integer currentLessonId = currentCourseLesson.getId();

    LessonCompleteResponse response =
        given()
                .header("X-Test-User-Id", userId)
            .contentType("application/json")
            .body(
                Map.of(
                    "lessonId", lessonId,
                    "courseId", course1.getId()))
            .when()
            .post(pathConstants.LESSONS_COMPLETIONS + pathConstants.SUBMIT_COMPLETED_LESSON)
            .then()
            .statusCode(200)
            .extract()
            .as(LessonCompleteResponse.class);

    assertThat(response.userId()).isEqualTo(userId);
    assertThat(response.totalScore()).isLessThanOrEqualTo(5);
    assertThat(response.accuracy()).isEqualTo(0);
    assertThat(response.updatedUserCourseProgress().currentLessonId()).isEqualTo(currentLessonId);
    assertThat(response.newUserScore()).isLessThanOrEqualTo(5);

  }

  @Test
  void submitLesson_happyPath_returnsCorrectResponse () {

    Integer userId = setupUserCompletionForTest(3, 0, 1, FIXED_TIMESTAMP_1, FIXED_TIMESTAMP_2);
    Integer lessonId = currentCourseLesson.getId();
    Integer nextLessonId = nextLesson.getId();

    LessonCompleteResponse response =
        given()
                .header("X-Test-User-Id", userId)
            .contentType("application/json")
            .body(
                Map.of(
                    "lessonId", lessonId,
                    "courseId", course1.getId()))
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
