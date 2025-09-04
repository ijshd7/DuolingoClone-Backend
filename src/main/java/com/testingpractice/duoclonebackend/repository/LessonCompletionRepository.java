package com.testingpractice.duoclonebackend.repository;

import com.testingpractice.duoclonebackend.entity.LessonCompletion;
import com.testingpractice.duoclonebackend.entity.LessonCompletionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface LessonCompletionRepository
        extends JpaRepository<LessonCompletion, LessonCompletionId> {

    boolean existsByIdUserIdAndIdLessonId(Integer userId, Integer lessonId);

    Optional<LessonCompletion> findByIdUserIdAndIdLessonId(Integer userId, Integer lessonId);

    @Query("""
    select lc.id.lessonId
    from LessonCompletion lc
    where lc.id.userId = :userId and lc.id.lessonId in :lessonIds
  """)
    List<Integer> findCompletedLessonIdsIn(@Param("userId") Integer userId,
                                           @Param("lessonIds") Collection<Integer> lessonIds);
}