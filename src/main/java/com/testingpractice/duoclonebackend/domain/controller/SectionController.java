package com.testingpractice.duoclonebackend.domain.controller;

import com.testingpractice.duoclonebackend.constants.pathConstants;
import com.testingpractice.duoclonebackend.dto.SectionDto;
import com.testingpractice.duoclonebackend.dto.UnitDto;
import com.testingpractice.duoclonebackend.service.SectionService;
import com.testingpractice.duoclonebackend.service.UnitService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(pathConstants.SECTIONS)
public class SectionController {

    private final SectionService sectionService;
    private final UnitService unitService;

    public SectionController(SectionService sectionService, UnitService unitService) {
        this.sectionService = sectionService;
        this.unitService = unitService;
    }

    @GetMapping(pathConstants.SECTIONS_FROM_IDS)
   public List<SectionDto> getSectionsByIds (@RequestParam List<Integer> sectionIds) {
       return sectionService.getSectionsByIds(sectionIds);
   }

    @GetMapping(pathConstants.SECTION_UNITS)
    public List<UnitDto> getUnitsBySection(@PathVariable Integer courseId) {
        return unitService.getUnitsBySection(courseId);
    }

    @GetMapping(pathConstants.SECTION_UNITS_IDS)
    public List<Integer> getUnitIdsBySection(@PathVariable Integer courseId) {
        return unitService.getUnitIdsBySection(courseId);
    }

}
