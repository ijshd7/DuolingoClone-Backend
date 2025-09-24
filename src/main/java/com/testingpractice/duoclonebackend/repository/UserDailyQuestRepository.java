package com.testingpractice.duoclonebackend.repository;

import com.testingpractice.duoclonebackend.entity.UserDailyQuest;
import com.testingpractice.duoclonebackend.entity.UserDailyQuestId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.time.LocalDate;
import java.util.List;

public interface UserDailyQuestRepository extends JpaRepository<UserDailyQuest, UserDailyQuestId> {

    List<UserDailyQuest> findAllByIdUserIdAndIdDate(Integer userId, LocalDate date);


}
