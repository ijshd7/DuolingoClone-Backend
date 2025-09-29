package com.testingpractice.duoclonebackend.dto;

import com.testingpractice.duoclonebackend.entity.Course;

import java.util.List;

public record ChangeCourseDto(UserDto newUser, List<Course> newCourses) {}
