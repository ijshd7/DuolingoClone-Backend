package com.testingpractice.duoclonebackend.service;

import com.testingpractice.duoclonebackend.dto.NewStreakCount;
import com.testingpractice.duoclonebackend.entity.User;
import java.sql.Timestamp;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import com.testingpractice.duoclonebackend.repository.UserRepository;
import com.testingpractice.duoclonebackend.utils.DateUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StreakServiceImpl implements StreakService {

  private final Clock clock;
  private final UserRepository userRepository;

  @Override
  @Transactional
  public NewStreakCount updateUserStreak(User user) {
    Timestamp last = user.getLastSubmission();
    LocalDate today = DateUtils.today(clock);

    int prev = user.getStreakLength();
    int next = prev;

    if (last == null) {
      next = 1;
    } else {
      LocalDate lastDay = last.toInstant().atZone(clock.getZone()).toLocalDate();
      boolean isToday = lastDay.equals(today);
      boolean isYesterday = lastDay.equals(today.minusDays(1));

      if (isYesterday || prev == 0) next = prev + 1;
      else if (!isToday)           next = 1;
    }

    user.setStreakLength(next);
    user.setLastSubmission(DateUtils.nowTs(clock));
    userRepository.save(user);
    return new NewStreakCount(prev, next);
  }
}
