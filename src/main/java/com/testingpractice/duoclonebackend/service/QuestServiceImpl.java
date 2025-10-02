package com.testingpractice.duoclonebackend.service;
import com.testingpractice.duoclonebackend.dto.QuestResponse;
import com.testingpractice.duoclonebackend.entity.QuestDefinition;
import com.testingpractice.duoclonebackend.entity.UserDailyQuest;
import com.testingpractice.duoclonebackend.enums.QuestCode;
import com.testingpractice.duoclonebackend.exception.ApiException;
import com.testingpractice.duoclonebackend.exception.ErrorCode;
import com.testingpractice.duoclonebackend.repository.QuestDefinitionRepository;
import com.testingpractice.duoclonebackend.repository.UserDailyQuestRepository;
import com.testingpractice.duoclonebackend.utils.DateUtils;
import jakarta.transaction.Transactional;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestServiceImpl implements QuestService {

  private final QuestDefinitionRepository questDefinitionRepository;
  private final UserDailyQuestRepository userDailyQuestRepository;
  private final MonthlyChallengeService monthlyChallengeService;
  private final Clock clock;

  @Override
  @Transactional
  public List<QuestResponse> getQuestsForUser(Integer userId) {

    LocalDate today = DateUtils.today(clock);

    List<QuestDefinition> questDefinitions = questDefinitionRepository.findAllByActive(true);

    refreshDailyActiveQuests(userId, questDefinitions, today);

    List<UserDailyQuest> userDailyQuests =
        userDailyQuestRepository.findAllByIdUserIdAndIdDate(userId, today);

    return parseToQuestionResponseList(questDefinitions, userDailyQuests);
  }

  @Override
  @Transactional
  public void updateQuestProgress(Integer userId, QuestCode questCode) {

    QuestDefinition questDefinition = questDefinitionRepository
            .findByCodeAndActiveTrue(questCode.name())
            .orElseThrow(() -> new ApiException(ErrorCode.QUEST_NOT_FOUND));

    LocalDate today = DateUtils.today(clock);
    UserDailyQuest userDailyQuest = userDailyQuestRepository
            .findByIdUserIdAndIdQuestDefIdAndIdDate(userId, questDefinition.getId(), today)
            .orElseGet(() -> createNewUserDailyQuest(questDefinition, userId, today));

    if (userDailyQuest.getProgress() < questDefinition.getTarget()) {
      userDailyQuest.setProgress(userDailyQuest.getProgress() + 1);
      monthlyChallengeService.addChallengeProgress(userId);
    }

  }

  private List<QuestResponse> parseToQuestionResponseList(
      List<QuestDefinition> questDefinitions, List<UserDailyQuest> userDailyQuests) {

    return userDailyQuests.stream()
        .map(
            userDailyQuest -> {
              QuestDefinition questDefinition =
                  questDefinitions.stream()
                      .filter(
                          definition ->
                              definition.getId().equals(userDailyQuest.getId().getQuestDefId()))
                      .findFirst()
                      .orElseThrow();
              return new QuestResponse(
                  questDefinition.getCode(),
                  userDailyQuest.getProgress(),
                  questDefinition.getTarget(),
                  questDefinition.isActive());
            })
        .toList();
  }

  @Override
  @Transactional
  public void refreshDailyActiveQuests(
      Integer userId, List<QuestDefinition> questDefinitions, LocalDate today) {
    for (QuestDefinition questDefinition : questDefinitions) {
      createNewUserDailyQuest(questDefinition, userId, today);
    }
  }

  @Transactional
  public UserDailyQuest createNewUserDailyQuest(
          QuestDefinition questDefinition, Integer userId, LocalDate today) {

    userDailyQuestRepository.upsertCreate(userId, questDefinition.getId(), today);

    return userDailyQuestRepository
            .findByIdUserIdAndIdQuestDefIdAndIdDate(userId, questDefinition.getId(), today)
            .orElseThrow(() -> new ApiException(ErrorCode.USER_DAILY_QUEST_NOT_FOUND));
  }
}
