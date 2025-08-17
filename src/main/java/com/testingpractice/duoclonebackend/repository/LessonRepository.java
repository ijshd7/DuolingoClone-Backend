package com.testingpractice.duoclonebackend.repository;

import com.testingpractice.duoclonebackend.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Integer> {
    List<Lesson> findAllByUnitId (Integer unitId);
}
