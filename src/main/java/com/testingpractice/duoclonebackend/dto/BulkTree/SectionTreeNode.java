package com.testingpractice.duoclonebackend.dto.BulkTree;

import com.testingpractice.duoclonebackend.dto.SectionDto;
import java.util.List;

public record SectionTreeNode(SectionDto section, List<UnitTreeNode> units) {}
