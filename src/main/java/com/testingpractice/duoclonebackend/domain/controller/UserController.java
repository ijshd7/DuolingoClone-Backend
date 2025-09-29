package com.testingpractice.duoclonebackend.domain.controller;

import com.testingpractice.duoclonebackend.constants.pathConstants;
import com.testingpractice.duoclonebackend.dto.UpdateAvatarRequest;
import com.testingpractice.duoclonebackend.dto.UserCourseProgressDto;
import com.testingpractice.duoclonebackend.dto.UserDto;
import com.testingpractice.duoclonebackend.service.UserCreationService;
import com.testingpractice.duoclonebackend.service.UserService;
import com.testingpractice.duoclonebackend.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(pathConstants.USERS)
public class UserController {

  private final UserService userService;
  private final UserCreationService userCreationService;

  @GetMapping(pathConstants.GET_USER_COURSE_PROGRESS)
  public UserCourseProgressDto getUserCourseProgress(
      @PathVariable Integer courseId, @PathVariable Integer userId) {
    return userService.getUserCourseProgress(courseId, userId);
  }

  @GetMapping(pathConstants.GET_USER_BY_ID)
  public UserDto getUserById (
          @PathVariable Integer userId
  ) {
    return userService.getUser(userId);
  }

  @GetMapping(pathConstants.GET_USERS_FROM_IDS)
  public List<UserDto> getUsersByIds (@RequestParam List<Integer> userIds) {
    return userService.getUsersFromIds(userIds);
  }

  @GetMapping(pathConstants.GET_AVATARS)
    public List<String> getAvatars() {
        return userCreationService.getDefaultProfilePics();
  }

  @PostMapping(pathConstants.UPDATE_AVATAR)
  public UserDto updateAvatar (
          @RequestBody UpdateAvatarRequest updateAvatarRequest
          ) {
    return userCreationService.updateAvatar(updateAvatarRequest.userId(), updateAvatarRequest.selectedAvatar());
  }




}
