package com.testingpractice.duoclonebackend.repository;

import com.testingpractice.duoclonebackend.entity.Exercise;

import java.util.Collection;
import java.util.List;

import com.testingpractice.duoclonebackend.entity.ExerciseOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseRepository extends JpaRepository<Exercise, Integer> {
  List<Exercise> findAllByLessonId(Integer lessonId);

  Integer lessonId(Integer lessonId);

    List<ExerciseOption> findAllByIdIn(Collection<Integer> ids);
}
