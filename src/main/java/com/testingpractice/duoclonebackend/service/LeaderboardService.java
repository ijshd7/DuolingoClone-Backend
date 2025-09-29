package com.testingpractice.duoclonebackend.service;

import com.testingpractice.duoclonebackend.dto.LeaderboardPageDto;

public interface LeaderboardService {

  LeaderboardPageDto getLeaderboardPage(String cursor, int limit);
}
