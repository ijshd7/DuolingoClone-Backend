package com.testingpractice.duoclonebackend.controller;

import com.testingpractice.duoclonebackend.constants.pathConstants;
import com.testingpractice.duoclonebackend.dto.FollowResponse;
import com.testingpractice.duoclonebackend.dto.LessonCompleteResponse;
import com.testingpractice.duoclonebackend.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static com.testingpractice.duoclonebackend.testutils.TestConstants.FIXED_TIMESTAMP_1;
import static com.testingpractice.duoclonebackend.testutils.TestConstants.FIXED_TIMESTAMP_2;
import static com.testingpractice.duoclonebackend.testutils.TestUtils.makeUser;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FollowControllerIT extends AbstractIntegrationTest{

    @BeforeEach
    void seed () {

    }

    @Test
    void followUser_noFollowExists_createsFollow () {

        User follower = userRepository.save(makeUser(course1.getId(), "testUser", "test", "user", "testemail", "pfp", 0, FIXED_TIMESTAMP_1, FIXED_TIMESTAMP_2, 0));
        Integer followerId = follower.getId();

        User followed = userRepository.save(makeUser(course1.getId(), "testUser2", "test2", "user2", "testemail2", "pfp", 0, FIXED_TIMESTAMP_1, FIXED_TIMESTAMP_2, 0));
        Integer followedId = followed.getId();

        FollowResponse response = submitLessonBody(followerId, followedId);

        assertThat(response).isNotNull();
        assertThat(response.followedNewStats().followerIds().size()).isEqualTo(1);
        assertThat(response.followedNewStats().followingIds().size()).isEqualTo(0);
        assertThat(response.followersNewStats().followingIds().size()).isEqualTo(1);
        assertThat(response.followersNewStats().followerIds().size()).isEqualTo(0);

    }

    private FollowResponse submitLessonBody (Integer userId, Integer followedId) {
        return
                given()
                        .header("X-Test-User-Id", userId)
                        .contentType("application/json")
                        .body(
                                Map.of(
                                        "followedId", followedId
                                )
                        )
                        .when()
                        .post(pathConstants.FOLLOWS + pathConstants.FOLLOW_USER)
                        .then()
                        .statusCode(200)
                        .extract()
                        .as(FollowResponse.class);
    }



}
