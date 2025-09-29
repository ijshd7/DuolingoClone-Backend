package com.testingpractice.duoclonebackend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import lombok.Data;

@Embeddable
@Data
public class UserMonthlyChallengeId implements Serializable {

  @Column(name = "user_id")
  private Integer userId;

  @Column(name = "challenge_def_id")
  private Integer challengeDefId;

  @Column(name = "year")
  private Integer year;

  @Column(name = "month")
  private Integer month;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof UserMonthlyChallengeId that)) return false;
    return Objects.equals(userId, that.userId)
        && Objects.equals(challengeDefId, that.challengeDefId)
        && Objects.equals(year, that.year)
        && Objects.equals(month, that.month);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, challengeDefId, year, month);
  }
}
