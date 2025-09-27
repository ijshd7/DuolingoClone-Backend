package com.testingpractice.duoclonebackend.domain.controller;

import com.testingpractice.duoclonebackend.constants.pathConstants;
import com.testingpractice.duoclonebackend.dto.LeaderboardPageDto;
import com.testingpractice.duoclonebackend.dto.UserDto;
import com.testingpractice.duoclonebackend.service.LeaderboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(pathConstants.LEADERBOARD)
@RequiredArgsConstructor
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    @GetMapping(pathConstants.GET_PAGINATED_LEADERBOARD)
    public LeaderboardPageDto get(
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "20") int limit) {
        limit = Math.min(Math.max(limit, 1), 100); // 1..100
        return leaderboardService.getPage(cursor, limit);
    }




}

