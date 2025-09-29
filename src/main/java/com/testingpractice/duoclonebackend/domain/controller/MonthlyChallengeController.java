package com.testingpractice.duoclonebackend.domain.controller;

import com.testingpractice.duoclonebackend.constants.pathConstants;
import com.testingpractice.duoclonebackend.dto.QuestResponse;
import com.testingpractice.duoclonebackend.service.MonthlyChallengeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(pathConstants.MONTHLY_CHALLENGES)
public class MonthlyChallengeController {

  private final MonthlyChallengeService monthlyChallengeService;

  @GetMapping(pathConstants.GET_MONTHLY_CHALLENGE_BY_USER)
  public QuestResponse getMonthlyChallenge(@AuthenticationPrincipal(expression = "id") Integer userId) {
    return monthlyChallengeService.getMonthlyChallengeForUser(userId);
  }
}
