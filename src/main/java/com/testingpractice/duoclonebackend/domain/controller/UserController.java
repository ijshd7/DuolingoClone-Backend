package com.testingpractice.duoclonebackend.domain.controller;

import com.testingpractice.duoclonebackend.constants.pathConstants;
import com.testingpractice.duoclonebackend.dto.UserCourseProgressDto;
import com.testingpractice.duoclonebackend.dto.UserDto;
import com.testingpractice.duoclonebackend.service.UserServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(pathConstants.USERS)
public class UserController {

  private final UserServiceImpl userServiceImpl;

  public UserController(UserServiceImpl userServiceImpl) {
    this.userServiceImpl = userServiceImpl;
  }

  @GetMapping(pathConstants.GET_USER_COURSE_PROGRESS)
  public UserCourseProgressDto getUserCourseProgress(
      @PathVariable Integer courseId, @PathVariable Integer userId) {
    return userServiceImpl.getUserCourseProgress(courseId, userId);
  }

  @GetMapping(pathConstants.GET_USER_BY_ID)
  public UserDto getUserById (
          @PathVariable Integer userId
  ) {
    return userServiceImpl.getUser(userId);
  }

}
