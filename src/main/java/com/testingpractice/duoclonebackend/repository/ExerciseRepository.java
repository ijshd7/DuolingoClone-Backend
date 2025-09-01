package com.testingpractice.duoclonebackend.repository;

import com.testingpractice.duoclonebackend.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseRepository extends JpaRepository<Exercise, Integer> {
}
