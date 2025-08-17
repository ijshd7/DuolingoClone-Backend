package com.testingpractice.duoclonebackend.repository;

import com.testingpractice.duoclonebackend.entity.Unit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UnitRepository extends JpaRepository<Unit, Integer> {

    public List<Unit> findAllByCourseId (Integer courseId);

}
