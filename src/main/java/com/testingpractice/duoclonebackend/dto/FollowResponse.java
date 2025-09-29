package com.testingpractice.duoclonebackend.dto;

public record FollowResponse(
    FollowFollowingListResponse followersNewStats, FollowFollowingListResponse followedNewStats) {}
