package com.testingpractice.duoclonebackend.service;

import com.testingpractice.duoclonebackend.dto.QuestResponse;
import com.testingpractice.duoclonebackend.entity.QuestDefinition;
import com.testingpractice.duoclonebackend.entity.UserDailyQuest;
import com.testingpractice.duoclonebackend.entity.UserDailyQuestId;
import com.testingpractice.duoclonebackend.repository.QuestDefinitionRepository;
import com.testingpractice.duoclonebackend.repository.UserDailyQuestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestService {

    private final QuestDefinitionRepository questDefinitionRepository;
    private final UserDailyQuestRepository userDailyQuestRepository;

    public List<QuestResponse> getQuestsForUser (Integer userId) {

        ZoneId tz = ZoneId.systemDefault();
        LocalDate today = LocalDate.now(tz);

        List<QuestDefinition> questDefinitions = questDefinitionRepository.findAllByActive(true);

        refreshDailyActiveQuests(userId, questDefinitions, today);

        List<UserDailyQuest> userDailyQuests =
                userDailyQuestRepository.findAllByIdUserIdAndIdDate(userId, today);

        return parseToQuestionResponseList(questDefinitions, userDailyQuests);


    }

    public void checkQuestCompletionStatus (Integer userId) {



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

    private void refreshDailyActiveQuests (Integer userId, List<QuestDefinition> questDefinitions, LocalDate today) {


        for (QuestDefinition questDefinition : questDefinitions) {
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
            }
        }


    }


}
