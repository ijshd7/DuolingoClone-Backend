package com.testingpractice.duoclonebackend.controller;

import com.testingpractice.duoclonebackend.constants.pathConstants;
import com.testingpractice.duoclonebackend.repository.UnitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static com.testingpractice.duoclonebackend.testutils.TestConstants.*;
import static com.testingpractice.duoclonebackend.testutils.TestUtils.makeUnit;
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
                .when().get(pathConstants.COURSES + pathConstants.GET_UNITS_BY_SECTION, 10)
                .then()
                .statusCode(200)
                .body("$", hasSize(2))
                .body("title", containsInAnyOrder(UNIT_1_TITLE, UNIT_2_TITLE))
                .body("[0].id", notNullValue())
                .body("[1].section", notNullValue());
    }



}