package com.testingpractice.duoclonebackend.repository;

import com.testingpractice.duoclonebackend.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Integer> {}
