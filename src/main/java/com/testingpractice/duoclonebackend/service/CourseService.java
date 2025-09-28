package com.testingpractice.duoclonebackend.service;

import com.testingpractice.duoclonebackend.dto.UserDto;
import com.testingpractice.duoclonebackend.entity.*;
import com.testingpractice.duoclonebackend.exception.ApiException;
import com.testingpractice.duoclonebackend.exception.ErrorCode;
import com.testingpractice.duoclonebackend.mapper.UserMapper;
import com.testingpractice.duoclonebackend.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final LookupService lookupService;
    private final UserMapper userMapper;
    private final SectionRepository sectionRepository;
    private final UnitRepository unitRepository;
    private final LessonRepository lessonRepository;
    private final UserCourseProgressRepository userCourseProgressRepository;

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Transactional
    public UserDto changeUserCourse(Integer userId, Integer newCourseId) {
        User user = lookupService.userOrThrow(userId);
        UserCourseProgress userCourseProgressOptional = userCourseProgressRepository.findByUserIdAndCourseId(userId, newCourseId);
        if (userCourseProgressOptional == null) {
            UserCourseProgress newProgress = new UserCourseProgress();
            newProgress.setUserId(userId);
            newProgress.setCourseId(newCourseId);
            newProgress.setCurrentLessonId(getFirstLessonIdOfCourse(newCourseId));
            newProgress.setUpdatedAt(Timestamp.from(Instant.now()));
            userCourseProgressRepository.save(newProgress);
        }

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
