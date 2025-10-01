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
import java.time.LocalDate;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MonthlyChallengeServiceImpl implements MonthlyChallengeService {

  private final MonthlyChallengeDefinitionRepository monthlyChallengeDefinitionRepository;
  private final UserMonthlyChallengeRepository userMonthlyChallengeRepository;

  @Override
  @Transactional
  public QuestResponse getMonthlyChallengeForUser(Integer userId) {
    ZoneId tz = ZoneId.systemDefault();
    LocalDate today = LocalDate.now(tz);
    MonthlyChallengeDefinition def = monthlyChallengeDefinitionRepository.findByActive(true);

    userMonthlyChallengeRepository.upsertCreate(userId, def.getId(), today.getYear(), today.getMonthValue());

    UserMonthlyChallengeId userMonthlyChallengeId = getMonthlyChallengeId(userId, def.getId(), today);

    var umc = userMonthlyChallengeRepository.findById(userMonthlyChallengeId)
            .orElseThrow();

    return new QuestResponse(def.getCode(), umc.getProgress(), def.getTarget(), def.isActive());
  }

  @Override
  @Transactional
  public void addChallengeProgress(Integer userId) {
    var tz = ZoneId.systemDefault();
    var today = LocalDate.now(tz);
    var def = monthlyChallengeDefinitionRepository.findByActive(true);

    userMonthlyChallengeRepository.upsertIncrement(
            userId, def.getId(), today.getYear(), today.getMonthValue(), 1);
  }

  private UserMonthlyChallengeId getMonthlyChallengeId(Integer userId, Integer challengeDefId, LocalDate date) {
    var id = new UserMonthlyChallengeId();
    id.setUserId(userId);
    id.setChallengeDefId(challengeDefId);
    id.setYear(date.getYear());
    id.setMonth(date.getMonthValue());
    return id;
  }

}
