package com.testingpractice.duoclonebackend.service;

import static com.testingpractice.duoclonebackend.testutils.TestConstants.*;
import static com.testingpractice.duoclonebackend.testutils.TestUtils.makeLesson;
import static org.assertj.core.api.Assertions.assertThat;

import com.testingpractice.duoclonebackend.dto.LessonDto;
import com.testingpractice.duoclonebackend.mapper.LessonMapperImpl;
import com.testingpractice.duoclonebackend.repository.LessonRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({LessonServiceImpl.class, LessonMapperImpl.class})
public class LessonServiceTest {

  @Autowired private LessonServiceImpl service;
  @Autowired private LessonRepository repo;

  @Test
  void getLessonsByUnit_returnsExpectedDtos() {

    repo.saveAll(
        List.of(
            makeLesson(LESSON_1_TITLE, 2, 1),
            makeLesson(LESSON_2_TITLE, 2, 2),
            makeLesson(LESSON_3_TITLE, 2, 3),
            makeLesson(LESSON_4_TITLE, 1, 3)));

    List<LessonDto> result = service.getLessonsByUnit(2, 1);
    assertThat(result).hasSize(3);
  }
}
