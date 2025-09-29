package com.testingpractice.duoclonebackend.service;

import com.testingpractice.duoclonebackend.dto.FollowFollowingListResponse;
import com.testingpractice.duoclonebackend.dto.FollowResponse;

public interface FollowService {

    FollowFollowingListResponse getFollowersAndFollowingForUser (Integer userId);
    FollowResponse handleFollow (Integer followerId, Integer followedId);
    FollowResponse handleUnfollow (Integer followerId, Integer followedId);
}
