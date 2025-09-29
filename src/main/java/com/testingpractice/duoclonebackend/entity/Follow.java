package com.testingpractice.duoclonebackend.entity;

import jakarta.persistence.*;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "follows")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Follow {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @Column(name = "follower_id")
  private Integer followerId;

  @Column(name = "followed_id")
  private Integer followedId;

  @Column(name = "created_at")
  private Timestamp createdAt;
}
