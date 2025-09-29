package com.testingpractice.duoclonebackend.service;

import com.testingpractice.duoclonebackend.dto.UserCourseProgressDto;
import com.testingpractice.duoclonebackend.dto.UserDto;
import com.testingpractice.duoclonebackend.entity.User;

import java.util.List;

public interface UserService {

    UserDto getUser(Integer userId);
    UserCourseProgressDto getUserCourseProgress(Integer courseId, Integer userId);
    List<UserDto> getUsersFromIds(List<Integer> userIds);
    void potentiallyResetStreak (User user);
}
