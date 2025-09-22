package com.testingpractice.duoclonebackend.service;

import com.testingpractice.duoclonebackend.dto.UserCourseProgressDto;
import com.testingpractice.duoclonebackend.dto.UserDto;

public interface UserService {

    UserDto getUser(Integer userId);
    UserCourseProgressDto getUserCourseProgress(Integer courseId, Integer userId);

}
