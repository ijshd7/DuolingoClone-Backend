package com.testingpractice.duoclonebackend.repository;

import com.testingpractice.duoclonebackend.entity.UserDailyQuest;
import com.testingpractice.duoclonebackend.entity.UserDailyQuestId;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDailyQuestRepository extends JpaRepository<UserDailyQuest, UserDailyQuestId> {

  List<UserDailyQuest> findAllByIdUserIdAndIdDate(Integer userId, LocalDate date);

  Optional<UserDailyQuest> findByIdUserIdAndIdQuestDefIdAndIdDate(
      Integer userId, Integer questDefId, LocalDate date);
}
