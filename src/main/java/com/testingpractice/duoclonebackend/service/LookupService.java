package com.testingpractice.duoclonebackend.service;

import com.testingpractice.duoclonebackend.entity.Lesson;
import com.testingpractice.duoclonebackend.entity.Unit;
import com.testingpractice.duoclonebackend.entity.User;
import com.testingpractice.duoclonebackend.exception.ApiException;
import com.testingpractice.duoclonebackend.exception.ErrorCode;
import com.testingpractice.duoclonebackend.repository.LessonRepository;
import com.testingpractice.duoclonebackend.repository.UnitRepository;
import com.testingpractice.duoclonebackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LookupService {

    private final UserRepository userRepository;
    private final LessonRepository lessonRepository;
    private final UnitRepository unitRepository;

    public User userOrThrow(int userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
    }

    public Lesson lessonOrThrow(int lessonId) {
        return lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ApiException(ErrorCode.LESSON_NOT_FOUND));
    }

    public Unit unitOrThrow(int unitId) {
        return unitRepository.findById(unitId)
                .orElseThrow(() -> new ApiException(ErrorCode.UNIT_NOT_FOUND));
    }

}
