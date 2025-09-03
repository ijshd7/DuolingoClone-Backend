package com.testingpractice.duoclonebackend.dto;

import java.util.List;

public record UnitDto(
        Integer id,
        String title,
        String description,
        Integer orderIndex,
        Integer sectionId,
        List<Integer> unitLessons
) {}
