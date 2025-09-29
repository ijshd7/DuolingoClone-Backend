package com.testingpractice.duoclonebackend.service;

import com.testingpractice.duoclonebackend.dto.LeaderboardPageDto;
import com.testingpractice.duoclonebackend.dto.UserDto;
import com.testingpractice.duoclonebackend.entity.User;
import com.testingpractice.duoclonebackend.mapper.UserMapper;
import com.testingpractice.duoclonebackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaderboardServiceImpl implements LeaderboardService {
    private final UserRepository repo;
    private final UserMapper mapper;

    @Override
    public LeaderboardPageDto getLeaderboardPage(String cursor, int limit) {
        List<User> users;

        if (cursor == null || cursor.isBlank()) {
            users = repo.findTopOrdered(PageRequest.of(0, limit));
        } else {
            String[] parts = cursor.split(":");
            int points = Integer.parseInt(parts[0]);
            int id = Integer.parseInt(parts[1]);
            users = repo.findAfterCursor(points, id, PageRequest.of(0, limit));
        }

        List<UserDto> dtos = users.stream().map(mapper::toDto).toList();

        String next = null;
        if (!users.isEmpty() && users.size() == limit) {
            User last = users.get(users.size() - 1);
            next = last.getPoints() + ":" + last.getId();
        }

        return new LeaderboardPageDto(dtos, next);
    }

}
