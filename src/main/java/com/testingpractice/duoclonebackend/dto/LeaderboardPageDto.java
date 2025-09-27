package com.testingpractice.duoclonebackend.dto;

import java.util.List;

public record LeaderboardPageDto(
        List<UserDto> users,
        String nextCursor
) {}