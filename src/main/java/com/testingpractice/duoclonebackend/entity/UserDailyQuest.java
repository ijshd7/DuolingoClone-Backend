package com.testingpractice.duoclonebackend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_daily_quest")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDailyQuest {

    @EmbeddedId private UserDailyQuestId id;
    @Column(nullable = false)
    private Integer progress = 0;

    @Column(name = "completed_at")
    private Timestamp completedAt;

    @Column(name = "reward_claimed", nullable = false)
    private boolean rewardClaimed = false;

}
