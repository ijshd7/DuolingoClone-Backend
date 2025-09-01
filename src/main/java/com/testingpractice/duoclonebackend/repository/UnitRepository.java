package com.testingpractice.duoclonebackend.repository;

import com.testingpractice.duoclonebackend.entity.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UnitRepository extends JpaRepository<Unit, Integer> {

    public List<Unit> findAllByCourseId (Integer courseId);

    @Query("select unit.id from Unit unit where unit.courseId = :courseId")
    List<Integer> findAllUnitIdsByCourseId(Integer courseId);

}
