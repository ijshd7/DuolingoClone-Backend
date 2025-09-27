package com.testingpractice.duoclonebackend.repository;

import com.testingpractice.duoclonebackend.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

public interface CourseRepository extends JpaRepository<Course, Integer> {



}
