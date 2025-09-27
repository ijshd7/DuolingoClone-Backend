package com.testingpractice.duoclonebackend.service;

import com.testingpractice.duoclonebackend.entity.Course;
import com.testingpractice.duoclonebackend.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }


}
