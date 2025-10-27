package com.testingpractice.duoclonebackend.controller;

import com.testingpractice.duoclonebackend.constants.pathConstants;
import com.testingpractice.duoclonebackend.dto.FlatTree.FlatSectionTreeResponse;
import com.testingpractice.duoclonebackend.service.CatalogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(pathConstants.CATALOG)
public class CatalogController {

    private final CatalogService catalogService;

    public CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping(pathConstants.SECTION_TREE)
    public ResponseEntity<FlatSectionTreeResponse> getSectionTree(@PathVariable Integer sectionId) {
        return ResponseEntity.ok(catalogService.getFlatCourseTree(sectionId));
    }


}
