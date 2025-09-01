package com.testingpractice.duoclonebackend.dto;

public record SectionDTO (
        Integer id,
        String title,
        Integer courseId,
        Integer orderIndex
){ }
