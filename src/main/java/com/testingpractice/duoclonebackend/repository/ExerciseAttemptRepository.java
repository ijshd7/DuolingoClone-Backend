package com.testingpractice.duoclonebackend.repository;

import com.testingpractice.duoclonebackend.entity.ExerciseAttempt;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ExerciseAttemptRepository extends JpaRepository<ExerciseAttempt, Integer> {

  @Query(
"""
  SELECT exerciseAttempt FROM ExerciseAttempt exerciseAttempt
  WHERE exerciseAttempt.userId = :userId
    AND exerciseAttempt.exerciseId IN :exerciseIds
    AND exerciseAttempt.isChecked = false
""")
  List<ExerciseAttempt> findAllByExerciseIdInAndUserIdAndUnchecked(
      @Param("exerciseIds") Collection<Integer> exerciseIds, @Param("userId") Integer userId);

  @Modifying
  @Query(
      """
    UPDATE ExerciseAttempt exerciseAttempt 
       SET exerciseAttempt.isChecked = true
     WHERE exerciseAttempt.userId = :userId
       AND exerciseAttempt.isChecked = false
       AND exerciseAttempt.exerciseId IN (
            SELECT e.id FROM Exercise e WHERE e.lessonId = :lessonId
       )
  """)
  int markUncheckedByUserAndLesson(
      @Param("userId") Integer userId, @Param("lessonId") Integer lessonId);
}
