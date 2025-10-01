package com.testingpractice.duoclonebackend.controller;

import com.testingpractice.duoclonebackend.auth.AuthUser;
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

import java.time.Clock;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Stream;

import static com.testingpractice.duoclonebackend.testutils.TestConstants.FIXED_TIMESTAMP_2;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Testcontainers
@Import({
        AbstractIntegrationTest.FixedClockConfig.class,
        AbstractIntegrationTest.TestSecurityConfig.class
})
public abstract class AbstractIntegrationTest {

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
    r.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop"); // or update/validate
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

    // Use TRUNCATE for all tables to ensure a clean state and reset auto-increment
    jdbc.execute("TRUNCATE TABLE users");
    jdbc.execute("ALTER TABLE users AUTO_INCREMENT = 1");
    jdbc.execute("TRUNCATE TABLE course");
    jdbc.execute("ALTER TABLE course AUTO_INCREMENT = 1");
    jdbc.execute("TRUNCATE TABLE sections");
    jdbc.execute("TRUNCATE TABLE units");
    jdbc.execute("TRUNCATE TABLE lessons");
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


}
