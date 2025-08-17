package com.testingpractice.duoclonebackend.service;

import com.testingpractice.duoclonebackend.dto.UnitDto;
import com.testingpractice.duoclonebackend.entity.Unit;
import com.testingpractice.duoclonebackend.mapper.UnitMapper;
import com.testingpractice.duoclonebackend.repository.UnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UnitService {

    private final UnitRepository unitRepository;
    private final UnitMapper unitMapper;

    @Autowired
    public UnitService (UnitRepository unitRepository, UnitMapper unitMapper) {
        this.unitRepository = unitRepository;
        this.unitMapper = unitMapper;
    }

    public List<UnitDto> getUnitsByCourse (Integer courseId) {
        List<Unit> units = unitRepository.findAllByCourseId(courseId);
        return unitMapper.toDtoList(units);
    }

}
