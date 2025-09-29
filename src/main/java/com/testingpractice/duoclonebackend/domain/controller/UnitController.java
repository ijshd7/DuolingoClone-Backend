package com.testingpractice.duoclonebackend.domain.controller;

import com.testingpractice.duoclonebackend.constants.pathConstants;
import com.testingpractice.duoclonebackend.dto.LessonDto;
import com.testingpractice.duoclonebackend.dto.UnitDto;
import com.testingpractice.duoclonebackend.service.LessonService;
import com.testingpractice.duoclonebackend.service.UnitService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(pathConstants.UNITS)
public class UnitController {

  private final LessonService lessonService;
  private final UnitService unitService;

  @GetMapping(pathConstants.GET_LESSONS_BY_UNIT)
  public List<LessonDto> getLessonsByUnit(
      @PathVariable Integer unitId, @AuthenticationPrincipal(expression = "id") Integer userId) {
    return lessonService.getLessonsByUnit(unitId, userId);
  }

  @GetMapping(pathConstants.GET_UNITS_FROM_IDS)
  public List<UnitDto> getUnitsByIds(@RequestParam List<Integer> unitIds) {
    return unitService.getUnitsByIds(unitIds);
  }

  @GetMapping(pathConstants.GET_LESSON_IDS_BY_UNIT)
  public List<Integer> getLessonIdsByUnit(@PathVariable Integer unitId) {
    return lessonService.getLessonIdsByUnit(unitId);
  }
}
