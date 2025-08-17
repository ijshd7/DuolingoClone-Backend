package com.testingpractice.duoclonebackend.mapper;

import com.testingpractice.duoclonebackend.dto.UnitDto;
import com.testingpractice.duoclonebackend.entity.Unit;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UnitMapper {

    UnitDto toDto(Unit unit);
    List<UnitDto> toDtoList(List<Unit> units);

}