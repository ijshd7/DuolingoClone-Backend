package com.testingpractice.duoclonebackend.service;

import com.testingpractice.duoclonebackend.dto.QuestResponse;
import com.testingpractice.duoclonebackend.entity.MonthlyChallengeDefinition;
import com.testingpractice.duoclonebackend.entity.UserMonthlyChallenge;

public interface MonthlyChallengeService {

  QuestResponse getMonthlyChallengeForUser(Integer userId);

  void addChallengeProgress(Integer userId);

  UserMonthlyChallenge getUserMCOrElseCreateNew(
      MonthlyChallengeDefinition monthlyChallengeDefinition, Integer userId, int year, int month);
}
