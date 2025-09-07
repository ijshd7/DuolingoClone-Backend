package com.testingpractice.duoclonebackend.repository;

import com.testingpractice.duoclonebackend.entity.Exercise;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseRepository extends JpaRepository<Exercise, Integer> {
  List<Exercise> findAllByLessonId(Integer lessonId);

  Integer lessonId(Integer lessonId);
}
