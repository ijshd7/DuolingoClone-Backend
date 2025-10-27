package com.testingpractice.duoclonebackend.dto.FlatTree;

import java.util.List;

public record FlatUnit(
        Integer id,
        Integer orderIndex,
        List<FlatLesson> lessons
) {}
