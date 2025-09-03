package com.testingpractice.duoclonebackend.service;

import com.testingpractice.duoclonebackend.dto.BulkTree.SectionTreeNode;
import com.testingpractice.duoclonebackend.dto.SectionDto;

import java.util.List;

public interface SectionService {
    List<SectionDto> getSectionsByIds (List<Integer> sectionIds);
    List<Integer> getSectionIdsByCourse (Integer courseId);
    SectionTreeNode getBulkSection (Integer sectionId);
}
