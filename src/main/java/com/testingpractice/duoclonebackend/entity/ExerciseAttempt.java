package com.testingpractice.duoclonebackend.entity;


import jakarta.persistence.*;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "exercise_attempts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExerciseAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "exercise_id")
    private Integer exerciseId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "is_checked", nullable = false)
    private boolean isChecked = false;


    @Column(name = "submitted_at")
    private Timestamp submittedAt;

    @Column(name = "option_id")
    private Integer optionId;

    @Column(name = "score")
    private Integer score;
}