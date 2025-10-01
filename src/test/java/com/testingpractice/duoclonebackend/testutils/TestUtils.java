package com.testingpractice.duoclonebackend.testutils;

import com.testingpractice.duoclonebackend.entity.Lesson;
import com.testingpractice.duoclonebackend.entity.LessonCompletion;
import com.testingpractice.duoclonebackend.entity.LessonCompletionId;
import com.testingpractice.duoclonebackend.entity.Unit;

import java.sql.Timestamp;

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

  public static Lesson makeLesson(String title, Integer unitId, Integer orderIndex, String lessonType) {
    return Lesson.builder().title(title).unitId(unitId).orderIndex(orderIndex).lessonType(lessonType).build();
  }

  public static LessonCompletion makeLessonCompletion(
          Integer userId, Integer lessonId, Integer courseId, Integer score
  ) {
    return LessonCompletion.builder()
            .id(new LessonCompletionId(userId, lessonId))
            .score(100)
            .courseId(1)
            .completedAt(new Timestamp(System.currentTimeMillis()))
            .build();
  }

}
