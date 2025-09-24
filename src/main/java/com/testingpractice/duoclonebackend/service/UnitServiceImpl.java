package com.testingpractice.duoclonebackend.service;

import com.testingpractice.duoclonebackend.dto.UnitDto;
import com.testingpractice.duoclonebackend.entity.Unit;
import com.testingpractice.duoclonebackend.mapper.UnitMapper;
import com.testingpractice.duoclonebackend.repository.UnitRepository;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UnitServiceImpl implements UnitService {

  private final UnitRepository unitRepository;
  private final UnitMapper unitMapper;

  @Override
  public List<UnitDto> getUnitsBySection(Integer sectionId) {
    List<Unit> units = unitRepository.findAllBySectionId(sectionId);
    return unitMapper.toDtoList(units);
  }

  @Override
  public List<UnitDto> getUnitsByIds(List<Integer> unitIds) {
    List<Unit> units = unitRepository.findAllById(unitIds);
    return unitMapper.toDtoList(units);
  }

  @Override
  public List<Integer> getUnitIdsBySection(Integer sectionId) {
    List<Integer> unitIds = unitRepository.findAllUnitIdsBySectionId(sectionId);
    return unitIds;
  }
}
