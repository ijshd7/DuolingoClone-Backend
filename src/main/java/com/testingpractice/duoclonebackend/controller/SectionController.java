package com.testingpractice.duoclonebackend.controller;

import com.testingpractice.duoclonebackend.constants.pathConstants;
import com.testingpractice.duoclonebackend.dto.BulkTree.SectionTreeNode;
import com.testingpractice.duoclonebackend.dto.SectionDto;
import com.testingpractice.duoclonebackend.dto.UnitDto;
import com.testingpractice.duoclonebackend.service.SectionService;
import com.testingpractice.duoclonebackend.service.UnitService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(pathConstants.SECTIONS)
public class SectionController {

  private final SectionService sectionService;
  private final UnitService unitService;

  @GetMapping(pathConstants.GET_SECTIONS_FROM_IDS)
  public List<SectionDto> getSectionsByIds(@RequestParam List<Integer> sectionIds) {
    return sectionService.getSectionsByIds(sectionIds);
  }

  @GetMapping(pathConstants.GET_UNITS_BY_SECTION)
  public List<UnitDto> getUnitsBySection(@PathVariable Integer sectionId) {
    return unitService.getUnitsBySection(sectionId);
  }

  @GetMapping(pathConstants.GET_UNIT_IDS_BY_SECTION)
  public List<Integer> getUnitIdsBySection(@PathVariable Integer sectionId) {
    return unitService.getUnitIdsBySection(sectionId);
  }

  @GetMapping(pathConstants.GET_BULK_SECTIONS)
  public SectionTreeNode getBulkSection(
      @PathVariable("sectionId") Integer sectionId,
      @AuthenticationPrincipal(expression = "id") Integer userId) {
    return sectionService.getBulkSection(sectionId, userId);
  }
}
