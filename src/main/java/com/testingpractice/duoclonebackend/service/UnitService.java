package com.testingpractice.duoclonebackend.service;
import com.testingpractice.duoclonebackend.dto.UnitDto;

import java.util.List;

public interface UnitService {

    List<UnitDto> getUnitsByCourse(Integer courseId);

}
