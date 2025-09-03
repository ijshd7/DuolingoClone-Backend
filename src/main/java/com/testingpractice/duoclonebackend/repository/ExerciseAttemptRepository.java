package com.testingpractice.duoclonebackend.repository;

import com.testingpractice.duoclonebackend.entity.ExerciseAttempt;
import com.testingpractice.duoclonebackend.entity.ExerciseOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface ExerciseAttemptRepository extends JpaRepository<ExerciseAttempt, Integer> {

    @Query("""
      SELECT ea FROM ExerciseAttempt ea
      JOIN Exercise e ON e.id = ea.exerciseId
      WHERE e.lessonId = :lessonId AND ea.userId = :userId
    """)

    List<ExerciseAttempt> findAllByExerciseIdInAndUserId(Collection<Integer> exerciseIds, Integer userId);
}
