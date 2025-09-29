package com.testingpractice.duoclonebackend.domain.controller;

import com.testingpractice.duoclonebackend.constants.pathConstants;
import com.testingpractice.duoclonebackend.dto.FollowFollowingListResponse;
import com.testingpractice.duoclonebackend.dto.FollowRequest;
import com.testingpractice.duoclonebackend.dto.FollowResponse;
import com.testingpractice.duoclonebackend.service.FollowService;
import com.testingpractice.duoclonebackend.service.FollowServiceImpl;
import lombok.RequiredArgsConstructor;
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
    public FollowResponse followUser (@RequestBody FollowRequest followRequest) {

        return followService.handleFollow(followRequest.followerId(), followRequest.followedId());

    }

    @PostMapping(pathConstants.UNFOLLOW_USER)
    public FollowResponse unfollowUser (@RequestBody FollowRequest followRequest) {
        return followService.handleUnfollow(followRequest.followerId(), followRequest.followedId());
    }

}
