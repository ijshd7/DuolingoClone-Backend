package com.testingpractice.duoclonebackend.service;

import com.testingpractice.duoclonebackend.dto.UserCourseProgressDto;
import com.testingpractice.duoclonebackend.entity.UserCourseProgress;
import com.testingpractice.duoclonebackend.mapper.UserCourseProgressMapper;
import com.testingpractice.duoclonebackend.repository.UserCourseProgressRepository;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl {

    private final UserCourseProgressRepository userCourseProgressRepository;
    private final UserCourseProgressMapper userCourseProgressMapper;

    public UserServiceImpl(UserCourseProgressRepository userCourseProgressRepository, UserCourseProgressMapper userCourseProgressMapper) {
        this.userCourseProgressRepository = userCourseProgressRepository;
        this.userCourseProgressMapper = userCourseProgressMapper;
    }

    public UserCourseProgressDto getUserCourseProgress (Integer courseId, Integer userId) {
        UserCourseProgress userCourseProgress = userCourseProgressRepository.findByUserIdAndCourseId(userId, courseId);
        return userCourseProgressMapper.toDto(userCourseProgress);
    }

}
