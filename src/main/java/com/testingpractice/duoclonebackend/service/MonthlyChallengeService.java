package com.testingpractice.duoclonebackend.service;

import com.testingpractice.duoclonebackend.dto.QuestResponse;
import com.testingpractice.duoclonebackend.entity.MonthlyChallengeDefinition;
import com.testingpractice.duoclonebackend.entity.UserMonthlyChallenge;
import com.testingpractice.duoclonebackend.entity.UserMonthlyChallengeId;
import com.testingpractice.duoclonebackend.exception.ApiException;
import com.testingpractice.duoclonebackend.exception.ErrorCode;
import com.testingpractice.duoclonebackend.repository.MonthlyChallengeDefinitionRepository;
import com.testingpractice.duoclonebackend.repository.UserMonthlyChallengeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class MonthlyChallengeService {

    private final MonthlyChallengeDefinitionRepository monthlyChallengeDefinitionRepository;
    private final UserMonthlyChallengeRepository userMonthlyChallengeRepository;

    @Transactional
    public QuestResponse getMonthlyChallengeForUser (Integer userId) {

       ZoneId tz = ZoneId.systemDefault();
       LocalDate today = LocalDate.now(tz);
       int year = today.getYear();
       int month = today.getMonthValue();

       MonthlyChallengeDefinition monthlyChallengeDefinition = monthlyChallengeDefinitionRepository.findByActive(true);

       UserMonthlyChallenge userMonthlyChallenge = getUserMCOrElseCreateNew(monthlyChallengeDefinition, userId, year, month);
       return new QuestResponse(monthlyChallengeDefinition.getCode(), userMonthlyChallenge.getProgress(), monthlyChallengeDefinition.getTarget(), monthlyChallengeDefinition.isActive());

   }

   @Transactional
   public void addChallengeProgress (Integer userId) {

       ZoneId tz = ZoneId.systemDefault();
       LocalDate today = LocalDate.now(tz);
       int year = today.getYear();
       int month = today.getMonthValue();

       MonthlyChallengeDefinition monthlyChallengeDefinition = monthlyChallengeDefinitionRepository.findByActive(true);
       UserMonthlyChallenge userMonthlyChallenge = getUserMCOrElseCreateNew(monthlyChallengeDefinition, userId, year, month);

       if (userMonthlyChallenge.getProgress() < monthlyChallengeDefinition.getTarget()) {
           userMonthlyChallenge.setProgress(userMonthlyChallenge.getProgress() +  1);
           userMonthlyChallengeRepository.save(userMonthlyChallenge);
       }

   }

   @Transactional
   public UserMonthlyChallenge getUserMCOrElseCreateNew (MonthlyChallengeDefinition monthlyChallengeDefinition, Integer userId, int year, int month) {

       UserMonthlyChallengeId id = new UserMonthlyChallengeId();
       id.setUserId(userId);
       id.setChallengeDefId(monthlyChallengeDefinition.getId());
       id.setYear(year);
       id.setMonth(month);

       if (!userMonthlyChallengeRepository.existsById(id)) {
           UserMonthlyChallenge umq = new UserMonthlyChallenge();
           umq.setId(id);
           umq.setProgress(0);
           umq.setRewardClaimed(false);
           userMonthlyChallengeRepository.save(umq);
           return umq;
       } else {
           return userMonthlyChallengeRepository.findById(id).orElseThrow(() -> new ApiException(ErrorCode.USER_DAILY_QUEST_NOT_FOUND));
       }

   }

}
