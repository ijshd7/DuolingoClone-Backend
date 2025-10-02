package com.testingpractice.duoclonebackend.controller;

import com.testingpractice.duoclonebackend.constants.pathConstants;
import com.testingpractice.duoclonebackend.dto.FollowResponse;
import com.testingpractice.duoclonebackend.entity.User;
import com.testingpractice.duoclonebackend.exception.ApiException;
import com.testingpractice.duoclonebackend.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static com.testingpractice.duoclonebackend.testutils.TestConstants.FIXED_TIMESTAMP_1;
import static com.testingpractice.duoclonebackend.testutils.TestConstants.FIXED_TIMESTAMP_2;
import static com.testingpractice.duoclonebackend.testutils.TestUtils.makeFollow;
import static com.testingpractice.duoclonebackend.testutils.TestUtils.makeUser;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FollowControllerIT extends AbstractIntegrationTest{

    @BeforeEach
    void seed () {

    }

    private String followPath = pathConstants.FOLLOWS + pathConstants.FOLLOW_USER;
    private String unfollowPath = pathConstants.FOLLOWS + pathConstants.UNFOLLOW_USER;

    @Test
    void followUser_noFollowExists_createsFollow () {

        User follower = userRepository.save(makeUser(course1.getId(), "testUser", "test", "user", "testemail", "pfp", 0, FIXED_TIMESTAMP_1, FIXED_TIMESTAMP_2, 0));
        Integer followerId = follower.getId();

        User followed = userRepository.save(makeUser(course1.getId(), "testUser2", "test2", "user2", "testemail2", "pfp", 0, FIXED_TIMESTAMP_1, FIXED_TIMESTAMP_2, 0));
        Integer followedId = followed.getId();

        FollowResponse response = submitFollowBody(followerId, followedId, followPath);

        assertThat(response).isNotNull();
        assertThat(response.followedNewStats().followerIds().size()).isEqualTo(1);
        assertThat(response.followedNewStats().followingIds().size()).isEqualTo(0);
        assertThat(response.followersNewStats().followingIds().size()).isEqualTo(1);
        assertThat(response.followersNewStats().followerIds().size()).isEqualTo(0);

    }

    @Test
    void unfollowUser_followExists_unfollowsUser () {

        User follower = userRepository.save(makeUser(course1.getId(), "testUser", "test", "user", "testemail", "pfp", 0, FIXED_TIMESTAMP_1, FIXED_TIMESTAMP_2, 0));
        Integer followerId = follower.getId();

        User followed = userRepository.save(makeUser(course1.getId(), "testUser2", "test2", "user2", "testemail2", "pfp", 0, FIXED_TIMESTAMP_1, FIXED_TIMESTAMP_2, 0));
        Integer followedId = followed.getId();

        followRepository.save(makeFollow(followerId, followedId, FIXED_TIMESTAMP_1));

        FollowResponse response = submitFollowBody(followerId, followedId, unfollowPath);

        assertThat(response).isNotNull();
        assertThat(response.followedNewStats().followerIds().size()).isEqualTo(0);
        assertThat(response.followedNewStats().followingIds().size()).isEqualTo(0);
        assertThat(response.followersNewStats().followingIds().size()).isEqualTo(0);
        assertThat(response.followersNewStats().followerIds().size()).isEqualTo(0);

    }

    @Test
    void followUser_followExists_doesNotUnfollow_throwsError () {

        User follower = userRepository.save(makeUser(course1.getId(), "testUser", "test", "user", "testemail", "pfp", 0, FIXED_TIMESTAMP_1, FIXED_TIMESTAMP_2, 0));
        Integer followerId = follower.getId();

        User followed = userRepository.save(makeUser(course1.getId(), "testUser2", "test2", "user2", "testemail2", "pfp", 0, FIXED_TIMESTAMP_1, FIXED_TIMESTAMP_2, 0));
        Integer followedId = followed.getId();

        followRepository.save(makeFollow(followerId, followedId, FIXED_TIMESTAMP_1));

        given()
                .header("X-Test-User-Id", followerId)
                .contentType("application/json")
                .body(Map.of("followedId", followedId))
                .when()
                .post(followPath)
                .then()
                .statusCode(ErrorCode.ALREADY_FOLLOWS.status().value())
                .body("title", equalTo(ErrorCode.ALREADY_FOLLOWS.name()))
                .body("detail", equalTo(ErrorCode.ALREADY_FOLLOWS.defaultMessage()))
                .body("status", equalTo(ErrorCode.ALREADY_FOLLOWS.status().value()));

    }

    @Test
    void unfollowUser_noFollowExists_doesNotUnfollow_throwsError () {

        User follower = userRepository.save(makeUser(course1.getId(), "testUser", "test", "user", "testemail", "pfp", 0, FIXED_TIMESTAMP_1, FIXED_TIMESTAMP_2, 0));
        Integer followerId = follower.getId();

        User followed = userRepository.save(makeUser(course1.getId(), "testUser2", "test2", "user2", "testemail2", "pfp", 0, FIXED_TIMESTAMP_1, FIXED_TIMESTAMP_2, 0));
        Integer followedId = followed.getId();

        given()
                .header("X-Test-User-Id", followerId)
                .contentType("application/json")
                .body(Map.of("followedId", followedId))
                .when()
                .post(unfollowPath)
                .then()
                .statusCode(ErrorCode.DOES_NOT_FOLLOW.status().value())
                .body("title", equalTo(ErrorCode.DOES_NOT_FOLLOW.name()))
                .body("detail", equalTo(ErrorCode.DOES_NOT_FOLLOW.defaultMessage()))
                .body("status", equalTo(ErrorCode.DOES_NOT_FOLLOW.status().value()));

    }



    private FollowResponse submitFollowBody(Integer userId, Integer followedId, String path) {
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
                        .post(path)
                        .then()
                        .statusCode(200)
                        .extract()
                        .as(FollowResponse.class);
    }



}
