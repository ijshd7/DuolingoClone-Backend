package com.testingpractice.duoclonebackend.repository;

import com.testingpractice.duoclonebackend.entity.UserDailyQuest;
import com.testingpractice.duoclonebackend.entity.UserDailyQuestId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDailyQuestRepository extends JpaRepository<UserDailyQuest, UserDailyQuestId> {}
