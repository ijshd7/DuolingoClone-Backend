package com.testingpractice.duoclonebackend.domain.controller;

import com.testingpractice.duoclonebackend.constants.pathConstants;
import com.testingpractice.duoclonebackend.entity.Course;
import com.testingpractice.duoclonebackend.service.CourseService;
import com.testingpractice.duoclonebackend.service.SectionService;
import com.testingpractice.duoclonebackend.service.UnitService;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(pathConstants.COURSES)
public class CourseController {

  private final UnitService unitService;
  private final SectionService sectionService;
  private final CourseService courseService;

  @GetMapping(pathConstants.GET_SECTION_IDS_BY_COURSE)
  public List<Integer> getSectionIdsByCourse(@PathVariable Integer courseId) {
    return sectionService.getSectionIdsByCourse(courseId);
  }

  @GetMapping(pathConstants.GET_ALL_COURSES)
  public List<Course> getCourses() {
    return courseService.getAllCourses();
  }

}
