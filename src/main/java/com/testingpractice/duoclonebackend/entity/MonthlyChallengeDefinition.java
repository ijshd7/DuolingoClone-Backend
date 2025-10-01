package com.testingpractice.duoclonebackend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthlyChallengeDefinition {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @Column(name = "code")
  private String code;

  @Column(name = "target")
  private Integer target;

  @Column(name = "reward_points")
  private Integer rewardPoints;

  @Column(name = "active")
  private boolean active;
}
