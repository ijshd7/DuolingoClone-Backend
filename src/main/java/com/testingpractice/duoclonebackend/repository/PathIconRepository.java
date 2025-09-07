package com.testingpractice.duoclonebackend.repository;

import com.testingpractice.duoclonebackend.entity.PathIcon;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PathIconRepository extends JpaRepository<PathIcon, Integer> {
  Optional<PathIcon> findByUnitId(Integer unitId);
}
