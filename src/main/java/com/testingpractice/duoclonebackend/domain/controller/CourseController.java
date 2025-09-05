package com.testingpractice.duoclonebackend.domain.controller;

import com.testingpractice.duoclonebackend.constants.pathConstants;
import com.testingpractice.duoclonebackend.dto.UnitDto;
import com.testingpractice.duoclonebackend.service.SectionService;
import com.testingpractice.duoclonebackend.service.UnitService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(pathConstants.COURSES)
public class CourseController {

  private final UnitService unitService;
  private final SectionService sectionService;

  public CourseController(UnitService unitService, SectionService sectionService) {
    this.unitService = unitService;
    this.sectionService = sectionService;
  }

  @GetMapping(pathConstants.GET_SECTION_IDS_BY_COURSE)
  public List<Integer> getSectionIdsByCourse(@PathVariable Integer courseId) {
    return sectionService.getSectionIdsByCourse(courseId);
  }
}
