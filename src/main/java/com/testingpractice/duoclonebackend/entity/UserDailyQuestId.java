package com.testingpractice.duoclonebackend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Embeddable
@Data
public class UserDailyQuestId implements Serializable {

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "quest_def_id")
    private Integer questDefId;

    @Column(name = "date")
    private LocalDate date;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDailyQuestId)) return false;
        UserDailyQuestId that = (UserDailyQuestId) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(questDefId, that.questDefId) &&
                Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, questDefId, date);
    }
}