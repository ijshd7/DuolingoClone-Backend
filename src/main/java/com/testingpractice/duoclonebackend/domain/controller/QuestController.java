package com.testingpractice.duoclonebackend.domain.controller;

import com.testingpractice.duoclonebackend.constants.pathConstants;
import com.testingpractice.duoclonebackend.dto.QuestResponse;
import com.testingpractice.duoclonebackend.service.QuestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(pathConstants.QUESTS)
public class QuestController {

    private final QuestService questService;

    @GetMapping(pathConstants.GET_QUESTS_BY_USER)
    public List<QuestResponse> getQuestsByUserId (
            @PathVariable Integer userId
    ) {
        return questService.getQuestsForUser(userId);
    }



}
