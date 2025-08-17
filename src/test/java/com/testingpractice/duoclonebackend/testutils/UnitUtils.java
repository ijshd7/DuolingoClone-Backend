package com.testingpractice.duoclonebackend.testutils;

import com.testingpractice.duoclonebackend.entity.Unit;


public class UnitUtils {


    public static Unit makeUnit(String title, int courseId, int section, int orderIndex) {
        return Unit.builder()
                .title(title)
                .description("Default description")
                .orderIndex(section)
                .courseId(courseId)
                .section(section)
                .build();
    }

}
