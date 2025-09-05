package com.testingpractice.duoclonebackend.repository;

import com.testingpractice.duoclonebackend.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findById(Integer id);
}
