package com.testingpractice.duoclonebackend.repository;

import com.testingpractice.duoclonebackend.entity.ExerciseOption;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ExerciseOptionRepository extends JpaRepository<ExerciseOption, Integer> {
  List<ExerciseOption> findAllByExerciseId(Integer exerciseId);

  List<ExerciseOption> findAllByIdIn(Collection<Integer> ids);

  @Query("""
    select eo.id
    from ExerciseOption eo
    where eo.exerciseId = :exerciseId and eo.answerOrder is not null
    order by eo.answerOrder asc, eo.id asc
  """)
  List<Integer> findCorrectOptionIds(@Param("exerciseId") Integer exerciseId);

}
