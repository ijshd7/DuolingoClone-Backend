package com.testingpractice.duoclonebackend.repository;

import com.testingpractice.duoclonebackend.entity.ExerciseOption;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseOptionRepository extends JpaRepository<ExerciseOption, Integer> {
  List<ExerciseOption> findAllByExerciseId(Integer exerciseId);
}
