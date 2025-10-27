package com.testingpractice.duoclonebackend.mapper;

import com.testingpractice.duoclonebackend.FlatUnitLessonRowProjection;
import com.testingpractice.duoclonebackend.dto.FlatTree.FlatSectionTreeResponse;
import com.testingpractice.duoclonebackend.dto.FlatTree.FlatLesson;
import com.testingpractice.duoclonebackend.dto.FlatTree.FlatUnit;
import org.mapstruct.Mapper;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Mapper(componentModel = "spring")
public interface FlatSectionTreeMapper {

    default FlatSectionTreeResponse toFlatTree(Integer sectionId, List<FlatUnitLessonRowProjection> rows) {
        Map<Integer, List<FlatUnitLessonRowProjection>> byUnit =
                rows.stream().collect(groupingBy(FlatUnitLessonRowProjection::getUnitId, LinkedHashMap::new, toList()));

        List<FlatUnit> units = byUnit.values().stream()
                .sorted(comparing(g -> g.get(0).getUnitOrder()))
                .map(this::mapFlatUnit)
                .toList();

        return new FlatSectionTreeResponse(sectionId, units);
    }

    private FlatUnit mapFlatUnit(List<FlatUnitLessonRowProjection> group) {
        FlatUnitLessonRowProjection head = group.get(0);
        List<FlatLesson> lessons = group.stream()
                .filter(r -> r.getLessonId() != null)
                .sorted(comparing(FlatUnitLessonRowProjection::getLessonOrder))
                .map(this::mapFlatLesson)
                .toList();

        return new FlatUnit(head.getUnitId(), head.getUnitOrder(), lessons);
    }

    private FlatLesson mapFlatLesson(FlatUnitLessonRowProjection row) {
        return new FlatLesson(row.getLessonId(), row.getLessonOrder());
    }
}