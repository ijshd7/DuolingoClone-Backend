package com.testingpractice.duoclonebackend.repository;

import com.testingpractice.duoclonebackend.entity.QuestDefinition;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestDefinitionRepository extends JpaRepository<QuestDefinition, Integer> {

  List<QuestDefinition> findAllByActive(boolean active);

  Optional<QuestDefinition> findByCode(String code);

  Optional<QuestDefinition> findByCodeAndActiveTrue(String code);
}
