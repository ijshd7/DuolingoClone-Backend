package com.testingpractice.duoclonebackend.service;
import com.testingpractice.duoclonebackend.dto.UnitDto;

import java.util.List;

public interface UnitService {

    List<UnitDto> getUnitsBySection(Integer sectionId);

    List<UnitDto> getUnitsByIds(List<Integer> unitIds);

    List<Integer> getUnitIdsBySection(Integer sectionId);

}
