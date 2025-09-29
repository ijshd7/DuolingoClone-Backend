package com.testingpractice.duoclonebackend.service;

import com.testingpractice.duoclonebackend.dto.NewStreakCount;
import com.testingpractice.duoclonebackend.entity.User;

public interface StreakService {

  NewStreakCount updateUserStreak(User user);
}
