package com.testingpractice.duoclonebackend.repository;

import com.testingpractice.duoclonebackend.entity.UserMonthlyChallenge;
import com.testingpractice.duoclonebackend.entity.UserMonthlyChallengeId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserMonthlyChallengeRepository extends JpaRepository<UserMonthlyChallenge, UserMonthlyChallengeId> {



}
