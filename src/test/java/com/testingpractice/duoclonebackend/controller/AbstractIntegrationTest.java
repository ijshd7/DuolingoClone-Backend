package com.testingpractice.duoclonebackend.controller;

import com.testingpractice.duoclonebackend.auth.AuthUser;
import com.testingpractice.duoclonebackend.entity.*;
import com.testingpractice.duoclonebackend.repository.*;
import io.restassured.RestAssured;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Timestamp;
import java.time.Clock;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Stream;

import static com.testingpractice.duoclonebackend.testutils.TestConstants.*;
import static com.testingpractice.duoclonebackend.testutils.TestConstants.LESSON_3_TITLE;
import static com.testingpractice.duoclonebackend.testutils.TestConstants.LESSON_4_TITLE;
import static com.testingpractice.duoclonebackend.testutils.TestConstants.LESSON_5_TITLE;
import static com.testingpractice.duoclonebackend.testutils.TestConstants.LESSON_6_TITLE;
import static com.testingpractice.duoclonebackend.testutils.TestUtils.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Testcontainers
@Import({
        AbstractIntegrationTest.FixedClockConfig.class,
        AbstractIntegrationTest.TestSecurityConfig.class
})
public abstract class AbstractIntegrationTest {


   protected Course course1;
   protected Lesson l1;
   protected Lesson l2;
   protected Lesson l3;
   protected Lesson l4;
   protected Lesson l5;
   protected Lesson l6;

   protected MonthlyChallengeDefinition monthlyChallengeDefinition;


  @Container
  @SuppressWarnings("resource")
  protected static final MySQLContainer<?> MYSQL =
          new MySQLContainer<>("mysql:8.0.39")
                  .withDatabaseName("duotest")
                  .withUsername("test")
                  .withPassword("test");

  @LocalServerPort
  protected int port;

  @DynamicPropertySource
  static void mysqlProps(DynamicPropertyRegistry r) {
    r.add("spring.datasource.url", MYSQL::getJdbcUrl);
    r.add("spring.datasource.username", MYSQL::getUsername);
    r.add("spring.datasource.password", MYSQL::getPassword);
    r.add("spring.datasource.driver-class-name", () -> "com.mysql.cj.jdbc.Driver");
    r.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    r.add("spring.jpa.properties.hibernate.dialect",
            () -> "org.hibernate.dialect.MySQL8Dialect");
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

  @Autowired
  JdbcTemplate jdbc;



  @BeforeAll
  static void restAssuredLogging() {
    RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
  }

  @BeforeEach
  void restAssuredBase() {
    RestAssured.baseURI = "http://localhost";
    RestAssured.port = port;
    cleanTestData();
    initializeCoursesSectionsAndLessons();
  }


  @TestConfiguration(proxyBeanMethods = false)
  @Profile("test")
  static class FixedClockConfig {
    @Bean
    Clock clock() {
      return Clock.fixed(FIXED_TIMESTAMP_2.toInstant(), ZoneId.systemDefault());
    }
  }

  @TestConfiguration(proxyBeanMethods = false)
  @Profile("test")
  static class TestSecurityConfig {
    @Bean
    SecurityFilterChain testFilter(HttpSecurity http) throws Exception {
      http.csrf(csrf -> csrf.disable())
              .authorizeHttpRequests(a -> a.anyRequest().authenticated())
              .addFilterBefore((req, res, chain) -> {
                var request = (HttpServletRequest) req;
                String header = request.getHeader("X-Test-User-Id");
                int uid = (header != null) ? Integer.parseInt(header) : 1;

                var auth = new UsernamePasswordAuthenticationToken(new AuthUser(uid), null, List.of());
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);

                chain.doFilter(req, res);
              }, UsernamePasswordAuthenticationFilter.class);
      return http.build();
    }
  }

  protected void cleanTestData() {

    jdbc.execute("SET FOREIGN_KEY_CHECKS = 0");

    jdbc.execute("TRUNCATE TABLE users");
    jdbc.execute("ALTER TABLE users AUTO_INCREMENT = 1");
    jdbc.execute("TRUNCATE TABLE course");
    jdbc.execute("ALTER TABLE course AUTO_INCREMENT = 1");
    jdbc.execute("TRUNCATE TABLE sections");
    jdbc.execute("TRUNCATE TABLE units");
    jdbc.execute("TRUNCATE TABLE lessons");
    jdbc.execute("TRUNCATE TABLE user_monthly_challenge");
    jdbc.execute("TRUNCATE TABLE exercises");
    jdbc.execute("TRUNCATE TABLE exercise_attempts");
    jdbc.execute("TRUNCATE TABLE lesson_completions");
    jdbc.execute("TRUNCATE TABLE user_course_progress");
    jdbc.execute("TRUNCATE TABLE quest_definition");
    jdbc.execute("TRUNCATE TABLE monthly_challenge_definition");
    jdbc.execute("TRUNCATE TABLE user_daily_quest");
    jdbc.execute("TRUNCATE TABLE user_monthly_challenge");
    jdbc.execute("TRUNCATE TABLE follows");
    jdbc.execute("TRUNCATE TABLE exercise_options");
    jdbc.execute("TRUNCATE TABLE exercise_attempt_option");


    jdbc.execute("SET FOREIGN_KEY_CHECKS = 1");
  }

  protected void initializeCoursesSectionsAndLessons () {


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

    questDefinitionRepository.saveAll(List.of(
            makeQuestDefition("STREAK", 1, 10, true),
            makeQuestDefition("ACCURACY", 2, 10, true),
            makeQuestDefition("PERFECT", 1, 10, true)
    ));

    monthlyChallengeDefinition = monthlyChallengeDefinitionRepository.save(makeMonthlyChallengeDefinition("COMPLETE_QUESTS", 30, 200, true));


  }


  protected Integer setupUserCompletionForTest (Integer correctScores, Integer submittedLessonId, Integer currentLessonId, Integer completedLessonsCount, Integer usersPoints, Integer streakLength, Timestamp lastSubmission, Timestamp AttemptTime) {

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


}
