package com.testingpractice.duoclonebackend.service;

import com.testingpractice.duoclonebackend.dto.UnitDto;
import com.testingpractice.duoclonebackend.entity.Unit;
import com.testingpractice.duoclonebackend.mapper.UnitMapperImpl;
import com.testingpractice.duoclonebackend.repository.UnitRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({UnitServiceImpl.class, UnitMapperImpl.class})
class UnitServiceTest {

    @Autowired private UnitServiceImpl service;
    @Autowired private UnitRepository repo;

    private Unit unit(String title, int courseId, int section) {
        Unit u = new Unit();
        u.setTitle(title);
        u.setCourseId(courseId);
        u.setSection(section);
        return u;
    }

    @Test
    void getUnitsByCourse_returnsExpectedDtos() {

        repo.saveAll(List.of(
                unit("Discuss a new Job", 10, 1),
                unit("Talk about your Habits", 10, 2),
                unit("Pack for a Vacation", 20, 1)
        ));

        List<UnitDto> result = service.getUnitsByCourse(10);

        assertThat(result).hasSize(2);
        assertThat(result).extracting(UnitDto::title)
                .containsExactlyInAnyOrder("Discuss a new Job", "Talk about your Habits");
    }



}