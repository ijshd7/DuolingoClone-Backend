package com.testingpractice.duoclonebackend.dto;

import java.util.List;

public record ChangeCourseDto(UserDto newUser, List<Integer> newCourses) {}
