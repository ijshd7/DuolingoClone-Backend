package com.testingpractice.duoclonebackend.service;

import com.testingpractice.duoclonebackend.dto.NextLessonDto;
import com.testingpractice.duoclonebackend.entity.Lesson;
import com.testingpractice.duoclonebackend.entity.Unit;
import com.testingpractice.duoclonebackend.exception.ApiException;
import com.testingpractice.duoclonebackend.exception.ErrorCode;
import com.testingpractice.duoclonebackend.repository.LessonRepository;
import com.testingpractice.duoclonebackend.repository.SectionRepository;
import com.testingpractice.duoclonebackend.repository.UnitRepository;
import jakarta.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurriculumNavigator {

  private final LessonRepository lessonRepository;
  private final UnitRepository unitRepository;
  private final SectionRepository sectionRepository;

  @Nullable
  public NextLessonDto getNextLesson(Lesson lesson, Integer userId, Integer courseId) {
    Lesson nextLessonInUnit =
        lessonRepository.findFirstByUnitIdAndOrderIndexGreaterThanOrderByOrderIndexAsc(
            lesson.getUnitId(), lesson.getOrderIndex());
    if (nextLessonInUnit != null) return new NextLessonDto(nextLessonInUnit, false);

    Optional<Unit> currentUnit = unitRepository.findById(lesson.getUnitId());
    if (currentUnit.isEmpty()) throw new ApiException(ErrorCode.UNIT_NOT_FOUND);

    Unit nextUnit =
        unitRepository.findFirstBySectionIdAndOrderIndexGreaterThanOrderByOrderIndexAsc(
            currentUnit.get().getSectionId(), currentUnit.get().getOrderIndex());
    if (nextUnit != null) {
      Lesson nextLesson = lessonRepository.findFirstByUnitIdOrderByOrderIndexAsc(nextUnit.getId());
      if (nextLesson == null) throw new ApiException(ErrorCode.LESSON_NOT_FOUND);
      return new NextLessonDto(nextLesson, false);
    } else {
      return new NextLessonDto(null, true);
    }
  }

  public List<Lesson> getLessonsBetweenInclusive(
      Integer courseId, Lesson from, Lesson to, Integer userId) {
    List<Lesson> out = new ArrayList<>();
    Lesson cur = from;
    out.add(cur);

    int guard = 0;
    while (!cur.getId().equals(to.getId())) {
      cur = getNextLesson(cur, userId, courseId).nextLesson();
      // Ok so course will never be completed here because its only for jump to
      if (cur == null) throw new ApiException(ErrorCode.LESSON_NOT_FOUND);
      out.add(cur);
      if (++guard > 10_000)
        throw new IllegalStateException("Guard tripped while traversing lessons");
    }
    return out;
  }
}
