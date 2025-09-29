package com.testingpractice.duoclonebackend.dto;

import java.util.List;

public record FollowFollowingListResponse(List<Integer> followingIds, List<Integer> followerIds) {}
