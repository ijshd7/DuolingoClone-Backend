package com.testingpractice.duoclonebackend.service;

import com.testingpractice.duoclonebackend.FlatUnitLessonRowProjection;
import com.testingpractice.duoclonebackend.dto.FlatTree.FlatSectionTreeResponse;
import com.testingpractice.duoclonebackend.mapper.FlatSectionTreeMapper;
import com.testingpractice.duoclonebackend.repository.UnitRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatalogService {

    private final UnitRepository unitRepository;
    private final FlatSectionTreeMapper flatSectionTreeMapper;

    public CatalogService(UnitRepository unitRepository, FlatSectionTreeMapper flatSectionTreeMapper) {
        this.unitRepository = unitRepository;
        this.flatSectionTreeMapper = flatSectionTreeMapper;
    }

    public FlatSectionTreeResponse getFlatCourseTree(Integer sectionId) {
        List<FlatUnitLessonRowProjection> rows = unitRepository.findFlatUnitLessonRowsBySectionId(sectionId);
        return flatSectionTreeMapper.toFlatTree(sectionId, rows);
    }




}
