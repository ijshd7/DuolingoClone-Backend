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
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@RequestMapping(pathConstants.AUTH)
public class AuthController {

  private final GoogleService googleService;
  private final JwtServiceImpl jwtService;
  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final AuthCookieService authCookieService;

  @PostMapping(pathConstants.GOOGLE_LOGIN)
  public ResponseEntity<UserDto> loginWithGoogle(@RequestBody TokenDto dto,
                                                 HttpServletResponse response) {
    UserDto userDto = googleService.loginOrRegisterWithCode(dto.getCode(), response);
    return ResponseEntity.ok(userDto);
  }

  @GetMapping("/me")
  public ResponseEntity<UserDto> getCurrentUser(@CookieValue(name = "jwt", required = false) String token) {
    if (token == null || !jwtService.isTokenValid(token)) return ResponseEntity.status(401).build();
    return ResponseEntity.ok(googleService.getCurrentUser(token));
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout(HttpServletResponse response) {
    authCookieService.clearJwt(response);
    return ResponseEntity.ok().build();
  }
}
