package com.testingpractice.duoclonebackend.service;

import com.testingpractice.duoclonebackend.dto.NewStreakCount;
import com.testingpractice.duoclonebackend.entity.User;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import org.springframework.stereotype.Service;

@Service
public class StreakServiceImpl implements StreakService {

  @Override
  public NewStreakCount updateUserStreak(User user) {

    Timestamp lastSubmission = user.getLastSubmission();
    ZoneId tz = ZoneId.systemDefault();
    LocalDate today = LocalDate.now(tz);

    Integer prev = user.getStreakLength();
    Integer next = prev;

    if (lastSubmission == null) {
      next = 1;
    } else {
      LocalDate lastDate = lastSubmission.toInstant().atZone(tz).toLocalDate();
      boolean isToday = lastDate.equals(today);
      boolean isYesterday = lastDate.equals(today.minusDays(1));

      if (isYesterday || prev == 0) {
        next = prev + 1;
      } else if (!isToday) {
        next = 1;
      }
    }

    user.setStreakLength(next);
    user.setLastSubmission(Timestamp.from(Instant.now()));

    return new NewStreakCount(prev, next);
  }
}
