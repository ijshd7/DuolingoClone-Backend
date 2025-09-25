package com.testingpractice.duoclonebackend.service;

import com.testingpractice.duoclonebackend.dto.FollowFollowingListResponse;
import com.testingpractice.duoclonebackend.dto.FollowResponse;
import com.testingpractice.duoclonebackend.entity.Follow;
import com.testingpractice.duoclonebackend.exception.ApiException;
import com.testingpractice.duoclonebackend.exception.ErrorCode;
import com.testingpractice.duoclonebackend.repository.FollowRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;

    public FollowFollowingListResponse getFollowersAndFollowingForUser (Integer userId) {

        List<Integer> followers = followRepository.findFollowerIdsByFollowedId(userId);
        List<Integer> following = followRepository.findFollowedIdsByFollowerId(userId);

        return new FollowFollowingListResponse(following, followers);

    }

    @Transactional
    public FollowResponse handleFollow (Integer followerId, Integer followedId) {

        boolean alreadyFollows = followRepository.existsByFollowerIdAndFollowedId(followerId, followedId);
        if (alreadyFollows) throw new ApiException(ErrorCode.ALREADY_FOLLOWS);

        Follow newFollow = new Follow();
        newFollow.setFollowerId(followerId);
        newFollow.setFollowedId(followedId);

        followRepository.save(newFollow);

        return getNewStatsForParties(followerId, followedId);

    }

    @Transactional
    public FollowResponse handleUnfollow (Integer followerId, Integer followedId) {

        boolean alreadyFollows = followRepository.existsByFollowerIdAndFollowedId(followerId, followedId);
        if (!alreadyFollows) throw new ApiException(ErrorCode.DOES_NOT_FOLLOW);

        Follow toDelete = followRepository.findByFollowerIdAndFollowedId(followerId, followedId);
        followRepository.delete(toDelete);

        return getNewStatsForParties(followerId, followedId);

    }

    private FollowResponse getNewStatsForParties (Integer followerId, Integer followedId) {
        FollowFollowingListResponse followerNewStats = getFollowersAndFollowingForUser(followerId);
        FollowFollowingListResponse followedNewStats = getFollowersAndFollowingForUser(followedId);

        return new FollowResponse(followerNewStats, followedNewStats);
    }


}
