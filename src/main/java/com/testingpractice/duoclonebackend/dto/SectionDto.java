package com.testingpractice.duoclonebackend.dto;

import java.util.List;

public record SectionDto(
        Integer id,
        String title,
        Integer courseId,
        Integer orderIndex,
        List<Integer> unitIds
){ }
