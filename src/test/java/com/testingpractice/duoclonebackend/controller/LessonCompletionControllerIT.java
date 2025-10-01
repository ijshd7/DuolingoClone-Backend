package com.testingpractice.duoclonebackend.controller;

import static com.testingpractice.duoclonebackend.testutils.TestConstants.*;
import static com.testingpractice.duoclonebackend.testutils.TestUtils.*;
import static io.restassured.RestAssured.given;
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
  private Lesson l4;
  private Lesson l5;
  private Lesson l6;

  private Course course1;


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

    l4 = savedLessons.get(3);
    l5 = savedLessons.get(4);
    l6 = savedLessons.get(5);
    Integer completedLessonId = l5.getId();

    questDefinitionRepository.saveAll(List.of(
            makeQuestDefition("STREAK", 1, 10, true),
            makeQuestDefition("ACCURACY", 2, 10, true),
            makeQuestDefition("PERFECT", 1, 10, true)
    ));

    monthlyChallengeDefinitionRepository.save(makeMonthlyChallengeDefinition("COMPLETE_QUESTS", 30, 200, true));

  }

  private Integer setupUserCompletionForTest (Integer correctScores, Integer submittedLessonId, Integer currentLessonId, Integer completedLessonsCount, Integer usersPoints, Integer streakLength, Timestamp lastSubmission, Timestamp AttemptTime) {

    User user = userRepository.save(makeUser(course1.getId(), "testuser", "test", "user", "emailOne", "default", usersPoints, FIXED_TIMESTAMP_1, lastSubmission, streakLength));
    Integer userId = user.getId();

    userCourseProgressRepository.save(makeUserCourseProgress(userId, course1.getId(), false, currentLessonId, FIXED_TIMESTAMP_1));

    List<Exercise> savedExercises = exerciseRepository.saveAll(
            List.of(
                    makeExercise(submittedLessonId, "Translate", 1),
                    makeExercise(submittedLessonId, "Translate", 2),
                    makeExercise(submittedLessonId, "Translate", 3)
            )
    );

    Exercise e1 = savedExercises.get(0);
    Exercise e2 = savedExercises.get(1);
    Exercise e3 = savedExercises.get(2);

    List<Integer> exercises = List.of(
            e1.getId(),
            e2.getId(),
            e3.getId()
    );

    List<Integer> completedLessons = List.of(
            l1.getId(),
            l2.getId(),
            l3.getId(),
            l4.getId(),
            l5.getId(),
            l6.getId()
    );

    for (int i = 0; i < 3; i++) {
      Integer score = i < correctScores ? 5 : 0;
      exerciseAttemptRepository.save(makeExerciseAttempt(exercises.get(i), userId, false, AttemptTime, i + 1, score));
    }

    for (int i = 0; i < completedLessonsCount; i++) {
      Integer lessonId = completedLessons.get(i);
      lessonCompletionRepository.save(makeLessonCompletion(userId, lessonId, course1.getId(), 10));
    }

    return userId;

  }


  @Test
  void submitLesson_happyPath_endCourse_returnsCourseComplete () {

    Integer lessonId = l6.getId();
    Integer userId = setupUserCompletionForTest(3, lessonId, l6.getId(), 5, 0, 1, FIXED_TIMESTAMP_1, FIXED_TIMESTAMP_2);


    LessonCompleteResponse response = submitLessonBody(userId, lessonId, course1.getId());

    assertThat(response.updatedUserCourseProgress().isComplete()).isTrue();
    assertThat(response.message()).isNotNull();
    assertThat(response.updatedUserCourseProgress().currentLessonId().equals(lessonId));


  }

  @Test
  void submitLesson_happyPath_jumpAhead_returnsListToUpdate () {

    Integer lessonId = l5.getId();
    Integer userId = setupUserCompletionForTest(3, lessonId, l3.getId(), 2, 0, 1, FIXED_TIMESTAMP_1, FIXED_TIMESTAMP_2);
    Integer nextLessonId = l6.getId();

    LessonCompleteResponse response = submitLessonBody(userId, lessonId, course1.getId());

    assertThat(response.updatedUserCourseProgress().currentLessonId()).isEqualTo(nextLessonId);
    assertThat(response.lessonsToUpdate().size()).isEqualTo(2);

  }


  @Test
  void submitLesson_happyPath_returnsCorrectResponse () {

    Integer lessonId = l5.getId();
    Integer userId = setupUserCompletionForTest(3, lessonId, l5.getId(), 4, 0, 1, FIXED_TIMESTAMP_1, FIXED_TIMESTAMP_2);
    Integer nextLessonId = l6.getId();

    LessonCompleteResponse response = submitLessonBody(userId, lessonId, course1.getId());

    assertThat(response.userId()).isEqualTo(userId);
    assertThat(response.lessonId()).isEqualTo(lessonId);
    assertThat(response.newUserScore()).isGreaterThan(15);
    assertThat(response.totalScore()).isGreaterThan(15);
    assertThat(response.accuracy()).isEqualTo(100);
    assertThat(response.message()).isNotNull();
    assertThat(response.updatedUserCourseProgress().currentLessonId()).isEqualTo(nextLessonId);

  }


  @Test
  void submitLesson_happyPath_returnsCorrectResponse_noUpdatedLessonId () {

    Integer lessonId = l4.getId();
    Integer userId = setupUserCompletionForTest(0, lessonId, l5.getId(), 4, 0, 1, FIXED_TIMESTAMP_1, FIXED_TIMESTAMP_2);
    Integer currentLessonId = l5.getId();

    LessonCompleteResponse response = submitLessonBody(userId, lessonId, course1.getId());
    assertThat(response.userId()).isEqualTo(userId);
    assertThat(response.totalScore()).isLessThanOrEqualTo(5);
    assertThat(response.accuracy()).isEqualTo(0);
    assertThat(response.updatedUserCourseProgress().currentLessonId()).isEqualTo(currentLessonId);
    assertThat(response.newUserScore()).isLessThanOrEqualTo(5);

  }

  private LessonCompleteResponse submitLessonBody (Integer userId, Integer lessonId, Integer courseId) {
    return
        given()
            .header("X-Test-User-Id", userId)
            .contentType("application/json")
            .body(
                Map.of(
                    "lessonId", lessonId,
                    "courseId", courseId))
            .when()
            .post(pathConstants.LESSONS_COMPLETIONS + pathConstants.SUBMIT_COMPLETED_LESSON)
            .then()
            .statusCode(200)
            .extract()
            .as(LessonCompleteResponse.class);
  }





}
