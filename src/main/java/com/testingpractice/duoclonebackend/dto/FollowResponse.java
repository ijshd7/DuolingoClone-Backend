package com.testingpractice.duoclonebackend.dto;

public record FollowResponse(
    Integer actorUserId,
    Integer followedUserId,
    FollowFollowingListResponse followersNewStats,
    FollowFollowingListResponse followedNewStats) {}
