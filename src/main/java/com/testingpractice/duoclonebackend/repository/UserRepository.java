package com.testingpractice.duoclonebackend.repository;

import com.testingpractice.duoclonebackend.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Integer> {
  Optional<User> findById(Integer id);

  @Query(
      """
    SELECT u FROM User u
    ORDER BY u.points DESC, u.id ASC
  """)
  List<User> findTopOrdered(Pageable pageable);

  // next pages using cursor
  @Query(
      """
    SELECT u FROM User u
    WHERE (u.points < :points)
       OR (u.points = :points AND u.id > :id)
    ORDER BY u.points DESC, u.id ASC
  """)
  List<User> findAfterCursor(@Param("points") int points, @Param("id") int id, Pageable pageable);

  Optional<Object> findByEmail(String email);
}
