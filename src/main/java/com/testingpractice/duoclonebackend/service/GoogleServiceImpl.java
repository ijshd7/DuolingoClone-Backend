    package com.testingpractice.duoclonebackend.service;

import com.testingpractice.duoclonebackend.auth.AuthCookieService;
import com.testingpractice.duoclonebackend.auth.GoogleOAuthClient;
import com.testingpractice.duoclonebackend.auth.GoogleTokenResponse;
import com.testingpractice.duoclonebackend.dto.GoogleUserInfo;
import com.testingpractice.duoclonebackend.dto.UserDto;
import com.testingpractice.duoclonebackend.entity.User;
import com.testingpractice.duoclonebackend.mapper.UserMapper;
import com.testingpractice.duoclonebackend.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
    @RequiredArgsConstructor
    public class GoogleServiceImpl implements GoogleService {

        private static final int DAY = 24 * 60 * 60;
        private final GoogleOAuthClient oauth;          // NEW
        private final UserRepository userRepository;
        private final UserMapper userMapper;
        private final JwtServiceImpl jwtService;
        private final UserCreationService userCreationService;
        private final AuthCookieService authCookieService; // NEW

        @Override
        @Transactional
        public UserDto loginOrRegisterWithCode(String code, HttpServletResponse response) {

            // 1) Get user info from Google
            GoogleTokenResponse token = oauth.exchangeCodeForAccessToken(code);
            GoogleUserInfo googleUser = oauth.fetchUserInfo(token.accessToken());

            // 2) Find user or register
            User user = (User) userRepository.findByEmail(googleUser.getEmail())
                    .orElseGet(() -> userCreationService.createUser(googleUser));

            // 3) Create JWT token
            String jwt = jwtService.createToken(user.getId());

            // 4) Set JWT token
            authCookieService.setJwt(response, jwt, DAY);

            return userMapper.toDto(user);
        }

    }