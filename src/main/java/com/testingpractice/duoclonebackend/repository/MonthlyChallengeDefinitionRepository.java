package com.testingpractice.duoclonebackend.repository;

import com.testingpractice.duoclonebackend.entity.MonthlyChallengeDefinition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MonthlyChallengeDefinitionRepository
    extends JpaRepository<MonthlyChallengeDefinition, Integer> {

  MonthlyChallengeDefinition findByActive(boolean active);
}
