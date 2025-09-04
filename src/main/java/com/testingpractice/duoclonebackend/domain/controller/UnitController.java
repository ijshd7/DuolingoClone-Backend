package com.testingpractice.duoclonebackend.domain.controller;

import com.testingpractice.duoclonebackend.constants.pathConstants;
import com.testingpractice.duoclonebackend.dto.LessonDto;
import com.testingpractice.duoclonebackend.dto.UnitDto;
import com.testingpractice.duoclonebackend.service.LessonService;
import com.testingpractice.duoclonebackend.service.UnitService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(pathConstants.UNITS)
public class UnitController {

    private final LessonService lessonService;
    private final UnitService unitService;

    public UnitController(LessonService lessonService, UnitService unitService) {
        this.lessonService = lessonService;
        this.unitService = unitService;
    }

    @GetMapping(pathConstants.UNIT_LESSONS)
    public List<LessonDto> getLessonsByUnit (@PathVariable Integer unitId, @PathVariable Integer userId) {
        return lessonService.getLessonsByUnit(unitId, userId);
    }

    @GetMapping(pathConstants.UNITS_FROM_IDS)
    public List<UnitDto> getUnitsByIds (@RequestParam List<Integer> unitIds) {
        return unitService.getUnitsByIds(unitIds);
    }

    @GetMapping(pathConstants.LESSONS_UNITS_IDS)
    public List<Integer> getLessonIdsByUnit(@PathVariable Integer unitId) {
        return lessonService.getLessonIdsByUnit(unitId);
    }


}
