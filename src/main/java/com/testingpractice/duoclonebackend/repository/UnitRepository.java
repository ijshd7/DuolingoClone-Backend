package com.testingpractice.duoclonebackend.repository;

import com.testingpractice.duoclonebackend.entity.Unit;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UnitRepository extends JpaRepository<Unit, Integer> {

    List<Unit> findAllBySectionId(Integer sectionId);

    @Query("select unit.id from Unit unit where unit.sectionId = :sectionId")
    List<Integer> findAllUnitIdsBySectionId(Integer sectionId);

    Unit findFirstBySectionIdAndOrderIndexGreaterThanOrderByOrderIndexAsc(Integer sectionId, Integer orderIndex);
    Unit findFirstBySectionIdOrderByOrderIndexAsc(Integer sectionId);

    List<Unit> findAllBySectionIdOrderByOrderIndexAsc(Integer sectionId);
}
