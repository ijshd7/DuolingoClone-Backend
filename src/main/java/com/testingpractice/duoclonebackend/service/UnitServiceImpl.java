package com.testingpractice.duoclonebackend.service;

import com.testingpractice.duoclonebackend.dto.UnitDto;
import com.testingpractice.duoclonebackend.entity.Unit;
import com.testingpractice.duoclonebackend.mapper.UnitMapper;
import com.testingpractice.duoclonebackend.repository.UnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UnitServiceImpl implements UnitService{

    private final UnitRepository unitRepository;
    private final UnitMapper unitMapper;

    @Autowired
    public UnitServiceImpl (UnitRepository unitRepository, UnitMapper unitMapper) {
        this.unitRepository = unitRepository;
        this.unitMapper = unitMapper;
    }

    @Override
    public List<UnitDto> getUnitsByCourse (Integer courseId) {
        List<Unit> units = unitRepository.findAllByCourseId(courseId);
        return unitMapper.toDtoList(units);
    }

    @Override
    public List<UnitDto> getUnitsByIds (List<Integer> unitIds) {
        List<Unit> units = unitRepository.findAllById(unitIds);
        return unitMapper.toDtoList(units);
    }

    @Override
    public List<Integer> getUnitIdsByCourse(Integer courseId) {
        List<Integer> unitIds = unitRepository.findAllUnitIdsByCourseId(courseId);
        return unitIds;
    }



}
