package com.testingpractice.duoclonebackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "exercise_attempt_option")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttemptOption {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @Column(name = "attempt_id")
  private Integer attemptId;

  @Column(name = "option_id")
  private Integer optionId;

  @Column(name = "position")
  private Integer position;
}
