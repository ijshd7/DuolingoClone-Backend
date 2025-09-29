package com.testingpractice.duoclonebackend.domain.controller;

import com.testingpractice.duoclonebackend.auth.AuthCookieService;
import com.testingpractice.duoclonebackend.service.GoogleService;
import com.testingpractice.duoclonebackend.service.JwtServiceImpl;
import com.testingpractice.duoclonebackend.dto.TokenDto;
import com.testingpractice.duoclonebackend.constants.pathConstants;
import com.testingpractice.duoclonebackend.dto.UserDto;
import com.testingpractice.duoclonebackend.entity.User;
import com.testingpractice.duoclonebackend.mapper.UserMapper;
import com.testingpractice.duoclonebackend.repository.UserRepository;
import com.testingpractice.duoclonebackend.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import static com.testingpractice.duoclonebackend.constants.pathConstants.GOOGLE_LOGOUT;

@RestController
@RequiredArgsConstructor
@RequestMapping(pathConstants.AUTH)
public class AuthController {

  private final GoogleService googleService;
  private final AuthCookieService authCookieService;
  private final UserService userService;

  @PostMapping(pathConstants.GOOGLE_LOGIN)
  public ResponseEntity<UserDto> loginWithGoogle(@RequestBody TokenDto dto,
                                                 HttpServletResponse response) {
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
