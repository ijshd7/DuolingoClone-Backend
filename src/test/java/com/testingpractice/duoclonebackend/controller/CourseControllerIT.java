package com.testingpractice.duoclonebackend.controller;

import com.testingpractice.duoclonebackend.DuocloneBackendApplication;
import com.testingpractice.duoclonebackend.constants.pathConstants;
import com.testingpractice.duoclonebackend.entity.Unit;
import com.testingpractice.duoclonebackend.repository.UnitRepository;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static com.testingpractice.duoclonebackend.constants.pathConstants.COURSE_UNITS;
import static com.testingpractice.duoclonebackend.testutils.TestConstants.*;
import static com.testingpractice.duoclonebackend.testutils.UnitUtils.makeUnit;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class CourseControllerIT extends AbstractIntegrationTest {

    @Autowired
    UnitRepository unitRepo;

    @BeforeEach
    void seed() {
        unitRepo.deleteAll();

        unitRepo.saveAll(List.of(
                makeUnit(UNIT_1_TITLE, 10, 1, 1),
                makeUnit(UNIT_2_TITLE, 10, 2, 5),
                makeUnit(UNIT_3_TITLE, 20, 1, 2)
        ));

    }

    @Test
    void getUnitsByCourse_returnsUnitsForThatCourse() {
        given()
                .when().get(pathConstants.COURSE_UNITS, 10)
                .then()
                .statusCode(200)
                .body("$", hasSize(2))
                .body("title", containsInAnyOrder(UNIT_1_TITLE, UNIT_2_TITLE))
                .body("[0].id", notNullValue())
                .body("[1].section", notNullValue());
    }



}