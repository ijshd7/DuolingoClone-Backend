package com.testingpractice.duoclonebackend.service;

import com.testingpractice.duoclonebackend.dto.UserDto;
import com.testingpractice.duoclonebackend.entity.Course;
import com.testingpractice.duoclonebackend.entity.User;
import com.testingpractice.duoclonebackend.mapper.UserMapper;
import com.testingpractice.duoclonebackend.repository.CourseRepository;
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

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Transactional
    public UserDto changeUserCourse(Integer userId, Integer newCourseId) {

        User user = lookupService.userOrThrow(userId);
        user.setCurrentCourseId(newCourseId);
        return userMapper.toDto(user);

    }

}
