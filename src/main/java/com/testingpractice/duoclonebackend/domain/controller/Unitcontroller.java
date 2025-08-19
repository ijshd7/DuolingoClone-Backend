package com.testingpractice.duoclonebackend.domain.controller;

import com.testingpractice.duoclonebackend.constants.pathConstants;
import com.testingpractice.duoclonebackend.dto.LessonDto;
import com.testingpractice.duoclonebackend.entity.Lesson;
import com.testingpractice.duoclonebackend.service.LessonService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(pathConstants.UNITS)
public class Unitcontroller {

    private final LessonService lessonService;

    public Unitcontroller(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @GetMapping(pathConstants.UNIT_LESSONS)
    public List<LessonDto> getLessonsByUnit (@PathVariable Integer unitId) {
        return lessonService.getLessonsByUnit(unitId);
    }


}
