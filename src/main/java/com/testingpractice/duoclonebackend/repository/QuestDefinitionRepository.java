package com.testingpractice.duoclonebackend.repository;

import com.testingpractice.duoclonebackend.entity.QuestDefinition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestDefinitionRepository extends JpaRepository<QuestDefinition, Integer> {


    List<QuestDefinition> findAllByActive(boolean active);
}
