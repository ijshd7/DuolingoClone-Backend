package com.testingpractice.duoclonebackend.service;

import com.testingpractice.duoclonebackend.dto.QuestResponse;
import com.testingpractice.duoclonebackend.entity.QuestDefinition;
import com.testingpractice.duoclonebackend.entity.UserDailyQuest;
import com.testingpractice.duoclonebackend.enums.QuestCode;

import java.time.LocalDate;
import java.util.List;

public interface QuestService {

    List<QuestResponse> getQuestsForUser(Integer userId);

    void updateQuestProgress(Integer userId, QuestCode questCode);

    void refreshDailyActiveQuests(Integer userId, List<QuestDefinition> questDefinitions, LocalDate today);

    UserDailyQuest createNewUserDailyQuest (QuestDefinition questDefinition, Integer userId, LocalDate today);

}
