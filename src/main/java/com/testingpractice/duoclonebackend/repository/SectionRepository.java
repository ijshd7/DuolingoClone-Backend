package com.testingpractice.duoclonebackend.repository;

import com.testingpractice.duoclonebackend.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface SectionRepository extends JpaRepository<Section, Integer> {
    List<Section> findByIdIn(List<Integer> sectionIds);

    @Query("select section.id from Section section where section.courseId= :courseId")
    List<Integer> findAllSectionIdsByCourseId(Integer courseId);

    Section findFirstByCourseIdAndOrderIndexGreaterThanOrderByOrderIndexAsc(Integer courseId, Integer orderIndex);
    Section findFirstByCourseIdOrderByOrderIndexAsc(Integer courseId);


}
