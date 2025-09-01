package com.testingpractice.duoclonebackend.service;

import com.testingpractice.duoclonebackend.dto.SectionDTO;
import com.testingpractice.duoclonebackend.entity.Section;
import com.testingpractice.duoclonebackend.mapper.SectionMapper;
import com.testingpractice.duoclonebackend.repository.SectionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SectionServiceImpl implements SectionService {

    private final SectionRepository sectionRepository;
    private final SectionMapper sectionMapper;


    public SectionServiceImpl(SectionRepository sectionRepository, SectionMapper sectionMapper) {
        this.sectionMapper = sectionMapper;
        this.sectionRepository = sectionRepository;
    }

    @Override
    public List<SectionDTO> getSectionsByIds (List<Integer> sectionIds) {
        List<Section> sections = sectionRepository.findAllById(sectionIds);
        return sectionMapper.toDtoList(sections);
    }

    @Override
    public List<Integer> getSectionIdsByCourse (Integer courseId) {
        List<Integer> sectionIds = sectionRepository.findAllSectionIdsByCourseId(courseId);
        return sectionIds;
    }




}
