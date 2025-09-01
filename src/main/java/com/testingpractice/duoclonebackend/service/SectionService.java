package com.testingpractice.duoclonebackend.service;

import com.testingpractice.duoclonebackend.dto.SectionDTO;

import java.util.List;

public interface SectionService {
    List<SectionDTO> getSectionsByIds (List<Integer> sectionIds);
    List<Integer> getSectionIdsByCourse (Integer courseId);
}
