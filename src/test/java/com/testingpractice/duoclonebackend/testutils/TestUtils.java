package com.testingpractice.duoclonebackend.testutils;

import com.testingpractice.duoclonebackend.entity.Lesson;
import com.testingpractice.duoclonebackend.entity.Unit;

public class TestUtils {

  public static Unit makeUnit(String title, int courseId, int section, int orderIndex) {
    return Unit.builder()
        .title(title)
        .description("Default description")
        .orderIndex(orderIndex)
        .courseId(courseId)
        .sectionId(section)
            .color("GREEN")
            .animationPath("Default animation path")
        .build();
  }

  public static Lesson makeLesson(String title, Integer unitId, Integer orderIndex) {
    return Lesson.builder().title(title).unitId(unitId).orderIndex(orderIndex).build();
  }
}
