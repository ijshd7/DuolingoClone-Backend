package com.testingpractice.duoclonebackend.controller;

import com.testingpractice.duoclonebackend.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static com.testingpractice.duoclonebackend.testutils.TestConstants.FIXED_TIMESTAMP_1;
import static com.testingpractice.duoclonebackend.testutils.TestConstants.FIXED_TIMESTAMP_2;
import static com.testingpractice.duoclonebackend.testutils.TestUtils.makeUser;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class QuestControllerIt extends AbstractIntegrationTest{

    @BeforeEach
    void seed () {

    }

    @Test
    void getUserDailyQuest_noneExisting_createsAndReturnsNew () {

        User user = userRepository.save(makeUser(course1.getId(), "testUser", "test", "user", "testemail", "pfp", 0, FIXED_TIMESTAMP_1, FIXED_TIMESTAMP_2, 0));
        Integer userId = user.getId();

    }

}
