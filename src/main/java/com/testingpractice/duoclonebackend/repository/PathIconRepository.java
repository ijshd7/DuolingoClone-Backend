package com.testingpractice.duoclonebackend.repository;

import com.testingpractice.duoclonebackend.entity.PathIcon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PathIconRepository extends JpaRepository<PathIcon, Integer> {
    Optional<PathIcon> findByUnitId(Integer unitId);
}
