package com.testingpractice.duoclonebackend.repository;

import com.testingpractice.duoclonebackend.entity.ExerciseAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface ExerciseAttemptRepository extends JpaRepository<ExerciseAttempt, Integer> {

    @Query("""
  SELECT ea FROM ExerciseAttempt ea
  WHERE ea.userId = :userId
    AND ea.exerciseId IN :exerciseIds
    AND ea.isChecked = false
""")
    List<ExerciseAttempt> findAllByExerciseIdInAndUserIdAndUnchecked(
            @Param("exerciseIds") Collection<Integer> exerciseIds,
            @Param("userId") Integer userId);

    @Modifying
    @Query("""
    UPDATE ExerciseAttempt ea
       SET ea.isChecked = true
     WHERE ea.userId = :userId
       AND ea.isChecked = false
       AND ea.exerciseId IN (
            SELECT e.id FROM Exercise e WHERE e.lessonId = :lessonId
       )
  """)
    int markUncheckedByUserAndLesson(@Param("userId") Integer userId,
                                     @Param("lessonId") Integer lessonId);
}
