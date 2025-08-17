package com.testingpractice.duoclonebackend.dto;

public record UnitDto(
        Integer id,
        String title,
        String description,
        Integer orderIndex,
        Integer section
) {}
