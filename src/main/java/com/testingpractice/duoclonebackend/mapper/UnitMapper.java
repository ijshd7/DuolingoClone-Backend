package com.testingpractice.duoclonebackend.mapper;

import com.testingpractice.duoclonebackend.dto.UnitDto;
import com.testingpractice.duoclonebackend.entity.Unit;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UnitMapper {

  UnitDto toDto(Unit unit);

  List<UnitDto> toDtoList(List<Unit> units);
}
