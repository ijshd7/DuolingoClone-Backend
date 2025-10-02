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



  @BeforeEach
  void seed() {


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
