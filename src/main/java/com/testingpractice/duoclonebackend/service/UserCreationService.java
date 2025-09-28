package com.testingpractice.duoclonebackend.service;

import com.testingpractice.duoclonebackend.dto.GoogleUserInfo;
import com.testingpractice.duoclonebackend.dto.UserDto;
import com.testingpractice.duoclonebackend.entity.User;
import com.testingpractice.duoclonebackend.repository.UserRepository;
import com.testingpractice.duoclonebackend.utils.UserCreationUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class UserCreationService {

    private final UserRepository userRepository;
    private final QuestService questService;
    private final MonthlyChallengeService monthlyChallengeService;

    @Transactional
    public User createUser(GoogleUserInfo googleUser) {

        User newUser = new User();
        newUser.setEmail(googleUser.getEmail());
        newUser.setFirstName(googleUser.getGivenName());
        newUser.setLastName(googleUser.getFamilyName());
        newUser.setUsername(UserCreationUtils.generateUsername(googleUser.getName()));
        newUser.setPfpSrc(UserCreationUtils.getRandomProfilePic());
        newUser.setPoints(0);
        newUser.setStreakLength(0);
        newUser.setCreatedAt(Timestamp.from(Instant.now()));
        userRepository.save(newUser);

        //This generates daily quests and monthly challenge for the new user
        questService.getQuestsForUser(newUser.getId());
        monthlyChallengeService.getMonthlyChallengeForUser(newUser.getId());

        return newUser;


    }




}
