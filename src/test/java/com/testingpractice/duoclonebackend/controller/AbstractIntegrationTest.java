package com.testingpractice.duoclonebackend.controller;

import com.testingpractice.duoclonebackend.repository.*;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public abstract class AbstractIntegrationTest {

  @Container
  @SuppressWarnings("resource")
  protected static final PostgreSQLContainer<?> POSTGRES =
      new PostgreSQLContainer<>("postgres:16-alpine")
          .withDatabaseName("duotest")
          .withUsername("test")
          .withPassword("test");
  @LocalServerPort protected int port;

  @DynamicPropertySource
  static void postgresProps(DynamicPropertyRegistry r) {
    r.add("spring.datasource.url", POSTGRES::getJdbcUrl);
    r.add("spring.datasource.username", POSTGRES::getUsername);
    r.add("spring.datasource.password", POSTGRES::getPassword);
    r.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
    r.add("spring.jpa.hibernate.ddl-auto", () -> "update");
  }

  @Autowired protected UnitRepository unitRepository;
  @Autowired protected SectionRepository sectionRepository;
  @Autowired protected CourseRepository courseRepository;
  @Autowired protected QuestDefinitionRepository questDefinitionRepository;
  @Autowired protected ExerciseAttemptRepository exerciseAttemptRepository;
  @Autowired protected ExerciseRepository exerciseRepository;
  @Autowired protected FollowRepository followRepository;
  @Autowired protected ExerciseOptionRepository exerciseOptionRepository;
  @Autowired protected ExerciseAttemptOptionRepository exerciseAttemptOptionRepository;
  @Autowired protected UserDailyQuestRepository userDailyQuestRepository;
  @Autowired protected MonthlyChallengeDefinitionRepository monthlyChallengeDefinitionRepository;
  @Autowired protected UserMonthlyChallengeRepository userMonthlyChallengeRepository;
  @Autowired protected UserRepository userRepository;
  @Autowired protected LessonRepository lessonRepository;
  @Autowired protected LessonCompletionRepository lessonCompletionRepository;
  @Autowired protected UserCourseProgressRepository userCourseProgressRepository;

  @BeforeAll
  static void restAssuredLogging() {
    RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
  }

  @BeforeEach
  void restAssuredBase() {
    RestAssured.baseURI = "http://localhost";
    RestAssured.port = port;
  }
}
