package com.testingpractice.duoclonebackend.service;

import com.testingpractice.duoclonebackend.entity.Lesson;
import com.testingpractice.duoclonebackend.entity.Section;
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
    public Lesson getNextLesson(Lesson lesson, Integer userId, Integer courseId) {

        //GET NEXT LESSON IN UNIT
        Lesson nextLessonInUnit =
                lessonRepository.findFirstByUnitIdAndOrderIndexGreaterThanOrderByOrderIndexAsc(
                        lesson.getUnitId(), lesson.getOrderIndex());
        if (nextLessonInUnit != null) return nextLessonInUnit;

        Optional<Unit> currentUnit = unitRepository.findById(lesson.getUnitId());
        if (currentUnit.isEmpty())
            throw new ApiException(ErrorCode.UNIT_NOT_FOUND);

        Unit nextUnit =
                unitRepository.findFirstBySectionIdAndOrderIndexGreaterThanOrderByOrderIndexAsc(
                        currentUnit.get().getSectionId(), currentUnit.get().getOrderIndex());
        if (nextUnit != null) {
            return lessonRepository.findFirstByUnitIdOrderByOrderIndexAsc(nextUnit.getId());
        }

        Optional<Section> currentSection = sectionRepository.findById(currentUnit.get().getSectionId());
        if (currentSection.isEmpty())
            throw new ApiException(ErrorCode.SECTION_NOT_FOUND);

        Section nextSection =
                sectionRepository.findFirstByCourseIdAndOrderIndexGreaterThanOrderByOrderIndexAsc(
                        currentSection.get().getCourseId(), currentSection.get().getOrderIndex());

        if (nextSection != null) {
            Unit firstUnitOfSection =
                    unitRepository.findFirstBySectionIdOrderByOrderIndexAsc(nextSection.getId());
            if (firstUnitOfSection == null)
                throw new ApiException(ErrorCode.COURSE_END);
            return lessonRepository.findFirstByUnitIdOrderByOrderIndexAsc(firstUnitOfSection.getId());
        }

        return null;
    }

    public List<Lesson> getLessonsBetweenInclusive(Integer courseId, Lesson from, Lesson to, Integer userId) {
        List<Lesson> out = new ArrayList<>();
        Lesson cur = from;
        out.add(cur);

        int guard = 0;
        while (!cur.getId().equals(to.getId())) {
            cur = getNextLesson(cur, userId, courseId);
            if (cur == null) throw new ApiException(ErrorCode.LESSON_NOT_FOUND);
            out.add(cur);
            if (++guard > 10_000) throw new IllegalStateException("Guard tripped while traversing lessons");
        }
        return out;
    }

}

