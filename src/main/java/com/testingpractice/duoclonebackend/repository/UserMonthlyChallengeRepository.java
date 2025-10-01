package com.testingpractice.duoclonebackend.repository;

import com.testingpractice.duoclonebackend.entity.UserMonthlyChallenge;
import com.testingpractice.duoclonebackend.entity.UserMonthlyChallengeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserMonthlyChallengeRepository
    extends JpaRepository<UserMonthlyChallenge, UserMonthlyChallengeId> {


    @Modifying
    @Query(value = """
    INSERT INTO user_monthly_challenge
      (user_id, challenge_def_id, year, month, progress, reward_claimed, completed_at)
    VALUES (:userId, :defId, :year, :month, 0, false, NULL)
    ON DUPLICATE KEY UPDATE user_id = user_id
    """, nativeQuery = true)
    int upsertCreate(@Param("userId") int userId,
                     @Param("defId") int defId,
                     @Param("year") int year,
                     @Param("month") int month);

    @Modifying
    @Query(value = """
    INSERT INTO user_monthly_challenge
      (user_id, challenge_def_id, year, month, progress, reward_claimed, completed_at)
    VALUES (:userId, :defId, :year, :month, :delta, false, NULL)
    ON DUPLICATE KEY UPDATE progress = progress + :delta
    """, nativeQuery = true)
    int upsertIncrement(@Param("userId") int userId,
                        @Param("defId") int defId,
                        @Param("year") int year,
                        @Param("month") int month,
                        @Param("delta") int delta);

}

