package com.testingpractice.duoclonebackend.repository;

import com.testingpractice.duoclonebackend.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExerciseRepository extends JpaRepository<Exercise, Integer> {
    List<Exercise> findAllByLessonId(Integer lessonId);
}
