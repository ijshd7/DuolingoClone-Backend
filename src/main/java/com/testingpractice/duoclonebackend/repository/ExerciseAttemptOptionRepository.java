package com.testingpractice.duoclonebackend.repository;

import com.testingpractice.duoclonebackend.entity.AttemptOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseAttemptOptionRepository extends JpaRepository<AttemptOption, Integer> {}
