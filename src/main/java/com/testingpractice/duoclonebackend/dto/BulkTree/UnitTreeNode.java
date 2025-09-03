package com.testingpractice.duoclonebackend.dto.BulkTree;

import com.testingpractice.duoclonebackend.dto.LessonDto;
import com.testingpractice.duoclonebackend.dto.UnitDto;
import com.testingpractice.duoclonebackend.entity.Unit;

import java.util.List;

public record UnitTreeNode(
        UnitDto unit,
        List<LessonDto> lessons
) {}
