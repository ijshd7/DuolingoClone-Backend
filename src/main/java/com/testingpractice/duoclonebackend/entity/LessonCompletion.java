package com.testingpractice.duoclonebackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "lesson_completions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LessonCompletion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "lesson_id")
    private Integer lessonId;

    @Column(name = "course_id")
    private Integer courseId;

    @Column(name = "score")
    private Integer score;

    @Column(name = "completed_at")
    private Timestamp completedAt;

}
