package com.testingpractice.duoclonebackend.entity;

import jakarta.persistence.*;
import java.sql.Timestamp;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @Column(name = "username")
  private String username;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "pfp_source")
  private String pfpSrc;

  @Column(name = "points")
  private Integer points;

  @Column(name = "created_at")
  private Timestamp createdAt;

  @Column(name = "last_submission")
  private Timestamp lastSubmission;

  @Column(name = "streak_length")
  private Integer streakLength;

  @Builder
  public User(
      String username,
      String firstName,
      String lastName,
      String pfpSrc,
      Integer points,
      Timestamp createdAt) {
    this.username = username;
    this.firstName = firstName;
    this.lastName = lastName;
    this.pfpSrc = pfpSrc;
    this.points = points;
    this.createdAt = createdAt;
  }
}
