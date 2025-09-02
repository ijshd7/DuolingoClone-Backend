package com.testingpractice.duoclonebackend.repository;

import com.testingpractice.duoclonebackend.entity.ExerciseOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExerciseOptionRepository extends JpaRepository<ExerciseOption, Integer> {
    List<ExerciseOption> findAllByExerciseId(Integer exerciseId);
}
