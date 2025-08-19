package com.testingpractice.duoclonebackend.domain.controller;

import com.testingpractice.duoclonebackend.constants.pathConstants;
import com.testingpractice.duoclonebackend.dto.UnitDto;
import com.testingpractice.duoclonebackend.service.UnitService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.testingpractice.duoclonebackend.constants.pathConstants.COURSES;

@RestController
@RequestMapping(pathConstants.COURSES)
public class CourseController {

    private final UnitService unitService;

    public CourseController(UnitService unitService) {
        this.unitService = unitService;
    }

    @GetMapping(pathConstants.COURSE_UNITS)
    public List<UnitDto> getUnitsByCourse(@PathVariable Integer courseId) {
        return unitService.getUnitsByCourse(courseId);
    }

}
