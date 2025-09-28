package com.testingpractice.duoclonebackend.service;

import com.testingpractice.duoclonebackend.dto.QuestResponse;
import com.testingpractice.duoclonebackend.entity.QuestDefinition;
import com.testingpractice.duoclonebackend.entity.UserDailyQuest;
import com.testingpractice.duoclonebackend.entity.UserDailyQuestId;
import com.testingpractice.duoclonebackend.enums.QuestCode;
import com.testingpractice.duoclonebackend.exception.ApiException;
import com.testingpractice.duoclonebackend.exception.ErrorCode;
import com.testingpractice.duoclonebackend.repository.QuestDefinitionRepository;
import com.testingpractice.duoclonebackend.repository.UserDailyQuestRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestService {

    private final QuestDefinitionRepository questDefinitionRepository;
    private final UserDailyQuestRepository userDailyQuestRepository;
    private final MonthlyChallengeService monthlyChallengeService;

    @Transactional
    public List<QuestResponse> getQuestsForUser (Integer userId) {

        ZoneId tz = ZoneId.systemDefault();
        LocalDate today = LocalDate.now(tz);

        List<QuestDefinition> questDefinitions = questDefinitionRepository.findAllByActive(true);

        refreshDailyActiveQuests(userId, questDefinitions, today);

        List<UserDailyQuest> userDailyQuests =
                userDailyQuestRepository.findAllByIdUserIdAndIdDate(userId, today);

        return parseToQuestionResponseList(questDefinitions, userDailyQuests);

    }

    @Transactional
    public void updateQuestProgress(Integer userId, QuestCode questCode) {

        QuestDefinition questDefinition = questDefinitionRepository
                .findByCodeAndActiveTrue(questCode.name())
                .orElseThrow(() -> new ApiException(ErrorCode.QUEST_NOT_FOUND));

        if (questDefinition != null) {
            ZoneId tz = ZoneId.systemDefault();
            LocalDate today = LocalDate.now(tz);

            UserDailyQuest userDailyQuest = userDailyQuestRepository
                    .findByIdUserIdAndIdQuestDefIdAndIdDate(userId, questDefinition.getId(), today)
                    .orElseGet(() -> createNewUserDailyQuest(questDefinition, userId, today));

            if (userDailyQuest.getProgress() < questDefinition.getTarget()) {
                userDailyQuest.setProgress(userDailyQuest.getProgress() + 1);
                monthlyChallengeService.addChallengeProgress(userId);
            }
        }

    }

    private List<QuestResponse> parseToQuestionResponseList (List<QuestDefinition> questDefinitions, List<UserDailyQuest> userDailyQuests) {

        return userDailyQuests.stream()
                .map(userDailyQuest -> {
                    QuestDefinition questDefinition = questDefinitions.stream()
                            .filter(definition -> definition.getId().equals(userDailyQuest.getId().getQuestDefId()))
                            .findFirst()
                            .orElseThrow();
                    return new QuestResponse(
                            questDefinition.getCode(),
                            userDailyQuest.getProgress(),
                            questDefinition.getTarget(),
                            questDefinition.isActive()
                    );
                })
                .toList();

    }

    @Transactional
    public void refreshDailyActiveQuests (Integer userId, List<QuestDefinition> questDefinitions, LocalDate today) {
        for (QuestDefinition questDefinition : questDefinitions) {
            createNewUserDailyQuest(questDefinition, userId, today);
        }
    }

    @Transactional
    public UserDailyQuest createNewUserDailyQuest (QuestDefinition questDefinition, Integer userId, LocalDate today) {

        UserDailyQuestId id = new UserDailyQuestId();
        id.setUserId(userId);
        id.setQuestDefId(questDefinition.getId());
        id.setDate(today);

        if (!userDailyQuestRepository.existsById(id)) {
            UserDailyQuest udq = new UserDailyQuest();
            udq.setId(id);
            udq.setProgress(0);
            udq.setRewardClaimed(false);
            userDailyQuestRepository.save(udq);
            return udq;
        } else {
            return userDailyQuestRepository.findById(id).orElseThrow(() -> new ApiException(ErrorCode.USER_DAILY_QUEST_NOT_FOUND));
        }


    }


}
