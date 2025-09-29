package com.testingpractice.duoclonebackend.domain.controller;

import com.testingpractice.duoclonebackend.constants.pathConstants;
import com.testingpractice.duoclonebackend.dto.FollowFollowingListResponse;
import com.testingpractice.duoclonebackend.dto.FollowRequest;
import com.testingpractice.duoclonebackend.dto.FollowResponse;
import com.testingpractice.duoclonebackend.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(pathConstants.FOLLOWS)
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @GetMapping(pathConstants.GET_FOLLOWS_BY_USER)
    public FollowFollowingListResponse getFollowersAndFollowingForUser (@PathVariable Integer userId) {
        return followService.getFollowersAndFollowingForUser(userId);
    }

    @PostMapping(pathConstants.FOLLOW_USER)
    public FollowResponse followUser (@RequestBody FollowRequest followRequest, @AuthenticationPrincipal(expression = "id") Integer followerId) {
        return followService.handleFollow(followerId, followRequest.followedId());
    }

    @PostMapping(pathConstants.UNFOLLOW_USER)
    public FollowResponse unfollowUser (@RequestBody FollowRequest followRequest, @AuthenticationPrincipal(expression = "id") Integer followerId) {
        return followService.handleUnfollow(followerId, followRequest.followedId());
    }

}
