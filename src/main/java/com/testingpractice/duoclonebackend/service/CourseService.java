package com.testingpractice.duoclonebackend.service;

import com.testingpractice.duoclonebackend.dto.UserDto;
import com.testingpractice.duoclonebackend.entity.*;
import com.testingpractice.duoclonebackend.exception.ApiException;
import com.testingpractice.duoclonebackend.exception.ErrorCode;
import com.testingpractice.duoclonebackend.mapper.UserMapper;
import com.testingpractice.duoclonebackend.repository.CourseRepository;
import com.testingpractice.duoclonebackend.repository.LessonRepository;
import com.testingpractice.duoclonebackend.repository.SectionRepository;
import com.testingpractice.duoclonebackend.repository.UnitRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final LookupService lookupService;
    private final UserMapper userMapper;
    private final SectionRepository sectionRepository;
    private final UnitRepository unitRepository;
    private final LessonRepository lessonRepository;

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Transactional
    public UserDto changeUserCourse(Integer userId, Integer newCourseId) {

        User user = lookupService.userOrThrow(userId);
        user.setCurrentCourseId(newCourseId);
        return userMapper.toDto(user);

    }

    public Integer getFirstLessonIdOfCourse(Integer courseId) {
        Course course = lookupService.courseOrThrow(courseId);

        Section section = sectionRepository.findFirstByCourseIdOrderByOrderIndexAsc(course.getId());
        if (section == null) {
            throw new ApiException(ErrorCode.SECTION_NOT_FOUND);
        }

        Unit firstUnit = unitRepository.findFirstBySectionIdOrderByOrderIndexAsc(section.getId());

        Lesson firstLesson = lessonRepository.findFirstByUnitIdOrderByOrderIndexAsc(firstUnit.getId());

        return firstLesson.getId();


    }

}
