package com.testingpractice.duoclonebackend.repository;

import com.testingpractice.duoclonebackend.entity.UserCourseProgress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCourseProgressRepository extends JpaRepository<UserCourseProgress, Integer> {
  UserCourseProgress findByUserId(Integer userId);

  UserCourseProgress findByUserIdAndCourseId(Integer userId, Integer courseId);
}
