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
                var principal = new AuthUser(1); // test user id
                var auth = new UsernamePasswordAuthenticationToken(principal, null, List.of());
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails((HttpServletRequest) req));
                SecurityContextHolder.getContext().setAuthentication(auth);
                chain.doFilter(req, res);
              }, UsernamePasswordAuthenticationFilter.class);
      return http.build();
    }
  }

  protected void cleanTestData() {
    exerciseAttemptOptionRepository.deleteAll();
    exerciseAttemptRepository.deleteAll();
    userDailyQuestRepository.deleteAll();
    userMonthlyChallengeRepository.deleteAll();
    followRepository.deleteAll();
    exerciseOptionRepository.deleteAll();
    exerciseRepository.deleteAll();
    lessonCompletionRepository.deleteAll();
    userCourseProgressRepository.deleteAll();
    lessonRepository.deleteAll();
    unitRepository.deleteAll();
    sectionRepository.deleteAll();
    courseRepository.deleteAll();
    questDefinitionRepository.deleteAll();
    monthlyChallengeDefinitionRepository.deleteAll();
    userRepository.deleteAll();
  }



}
