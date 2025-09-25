package com.testingpractice.duoclonebackend.domain.controller;

import com.testingpractice.duoclonebackend.constants.pathConstants;
import com.testingpractice.duoclonebackend.dto.FollowFollowingListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(pathConstants.FOLLOWS)
@RequiredArgsConstructor
public class FollowController {

    @GetMapping(pathConstants.GET_FOLLOWS_BY_USER)
    public FollowFollowingListResponse getFollowersAndFollowingForUser () {



    }

}
