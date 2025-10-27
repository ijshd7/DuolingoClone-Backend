package com.testingpractice.duoclonebackend.dto.FlatTree;

import java.util.List;

public record FlatSectionTreeResponse(
        Integer sectionId,
        List<FlatUnit> units
) {}
