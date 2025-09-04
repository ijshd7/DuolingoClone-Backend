package com.testingpractice.duoclonebackend.repository;

import com.testingpractice.duoclonebackend.entity.LessonCompletion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface LessonCompletionRepository extends JpaRepository<LessonCompletion, Integer> {

    boolean existsByUserIdAndLessonId(Integer userId, Integer lessonId);
    Optional<LessonCompletion> findByUserIdAndLessonId(Integer userId, Integer lessonId);
    @Query("""
    select lc.lessonId
    from LessonCompletion lc
    where lc.userId = :userId and lc.lessonId in :lessonIds
  """)
    List<Integer> findCompletedLessonIdsIn(@Param("userId") Integer userId,
                                           @Param("lessonIds") Collection<Integer> lessonIds);
}
