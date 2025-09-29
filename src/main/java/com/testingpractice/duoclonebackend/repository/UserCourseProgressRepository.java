package com.testingpractice.duoclonebackend.repository;

import com.testingpractice.duoclonebackend.entity.UserCourseProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserCourseProgressRepository extends JpaRepository<UserCourseProgress, Integer> {
  UserCourseProgress findByUserId(Integer userId);

  UserCourseProgress findByUserIdAndCourseId(Integer userId, Integer courseId);

  List<UserCourseProgress> findAllByUserId(Integer userId);
}
