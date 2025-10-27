package com.testingpractice.duoclonebackend.repository;

import com.testingpractice.duoclonebackend.FlatUnitLessonRowProjection;
import com.testingpractice.duoclonebackend.entity.Unit;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UnitRepository extends JpaRepository<Unit, Integer> {

  List<Unit> findAllBySectionId(Integer sectionId);

  @Query("select unit.id from Unit unit where unit.sectionId = :sectionId")
  List<Integer> findAllUnitIdsBySectionId(Integer sectionId);

  @Query(value = """
    SELECT
      u.id           AS unitId,
      u.order_index  AS unitOrder,
      l.id           AS lessonId,
      l.order_index  AS lessonOrder
    FROM units u
    LEFT JOIN lessons l ON l.unit_id = u.id
    WHERE u.section_id = :sectionId
    ORDER BY u.order_index, l.order_index
    """, nativeQuery = true)
  List<FlatUnitLessonRowProjection> findFlatUnitLessonRowsBySectionId(
          @Param("sectionId") Integer sectionId
  );

  Unit findFirstBySectionIdAndOrderIndexGreaterThanOrderByOrderIndexAsc(
      Integer sectionId, Integer orderIndex);

  Unit findFirstBySectionIdOrderByOrderIndexAsc(Integer sectionId);

  List<Unit> findAllBySectionIdOrderByOrderIndexAsc(Integer sectionId);

  Integer sectionId(Integer sectionId);
}
