package com.testingpractice.duoclonebackend.entity;

import jakarta.persistence.*;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_course_progress")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCourseProgress {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @Column(name = "user_id")
  private Integer userId;

  @Column(name = "course_id")
  private Integer courseId;

  @Column(name = "is_complete")
  private Boolean isComplete;

  @Column(name = "current_lesson_id")
  private Integer currentLessonId;

  @Column(
      name = "updated_at",
      columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
  private Timestamp updatedAt;

}
