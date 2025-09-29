package com.testingpractice.duoclonebackend.repository;

import com.testingpractice.duoclonebackend.entity.Follow;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FollowRepository extends JpaRepository<Follow, Integer> {


    @Query("select f.followedId from Follow f where f.followerId = :userId")
    List<Integer> findFollowedIdsByFollowerId(@Param("userId") Integer userId);

    @Query("select f.followerId from Follow f where f.followedId = :userId")
    List<Integer> findFollowerIdsByFollowedId(@Param("userId") Integer userId);

    boolean existsByFollowerIdAndFollowedId(Integer followerId, Integer followedId);

    Follow findByFollowerIdAndFollowedId(Integer followerId, Integer followedId);
}
