package com.testingpractice.duoclonebackend.repository;

import com.testingpractice.duoclonebackend.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Integer> {
    List<Lesson> findAllByUnitId (Integer unitId);

    @Query("select lesson.id from Lesson lesson where lesson.unitId = :unitId")
    List<Integer> findAllLessonIdsByUnitId(Integer unitId);

    Lesson findFirstByUnitIdAndOrderIndexGreaterThanOrderByOrderIndexAsc(Integer unitId, Integer orderIndex);
    Lesson findFirstByUnitIdOrderByOrderIndexAsc(Integer unitId);
}
