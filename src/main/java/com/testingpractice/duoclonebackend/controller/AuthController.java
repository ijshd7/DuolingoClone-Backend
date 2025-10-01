package com.testingpractice.duoclonebackend.controller;

import com.testingpractice.duoclonebackend.auth.AuthCookieService;
import com.testingpractice.duoclonebackend.constants.pathConstants;
import com.testingpractice.duoclonebackend.dto.TokenDto;
import com.testingpractice.duoclonebackend.dto.UserDto;
import com.testingpractice.duoclonebackend.service.GoogleService;
import com.testingpractice.duoclonebackend.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(pathConstants.AUTH)
public class AuthController {

  private final GoogleService googleService;
  private final AuthCookieService authCookieService;
  private final UserService userService;

  @PostMapping(pathConstants.GOOGLE_LOGIN)
  public ResponseEntity<UserDto> loginWithGoogle(
      @RequestBody TokenDto dto, HttpServletResponse response) {
    UserDto userDto = googleService.loginOrRegisterWithCode(dto.getCode(), response);
    return ResponseEntity.ok(userDto);
  }

  @GetMapping(pathConstants.AUTH_ME)
  public ResponseEntity<UserDto> getCurrentUser(
      @AuthenticationPrincipal(expression = "id") Integer userId) {
    return ResponseEntity.ok(userService.getUser(userId));
  }

  @PostMapping(pathConstants.GOOGLE_LOGOUT)
  public ResponseEntity<Void> logout(HttpServletResponse response) {
    authCookieService.clearJwt(response);
    return ResponseEntity.ok().build();
  }
}
