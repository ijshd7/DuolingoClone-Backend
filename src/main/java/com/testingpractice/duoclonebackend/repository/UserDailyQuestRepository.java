package com.testingpractice.duoclonebackend.repository;

import com.testingpractice.duoclonebackend.entity.UserDailyQuest;
import com.testingpractice.duoclonebackend.entity.UserDailyQuestId;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserDailyQuestRepository extends JpaRepository<UserDailyQuest, UserDailyQuestId> {

  List<UserDailyQuest> findAllByIdUserIdAndIdDate(Integer userId, LocalDate date);

  Optional<UserDailyQuest> findByIdUserIdAndIdQuestDefIdAndIdDate(
      Integer userId, Integer questDefId, LocalDate date);

  @Modifying
  @Query(value = """
    INSERT INTO user_daily_quest (user_id, quest_def_id, date, progress, reward_claimed)
    VALUES (?1, ?2, ?3, 0, false)
    ON DUPLICATE KEY UPDATE user_id = user_id
  """, nativeQuery = true)
  void upsertCreate(int userId, int questDefId, LocalDate date);

}
