    package com.testingpractice.duoclonebackend.service;

    import com.testingpractice.duoclonebackend.auth.AuthCookieService;
    import com.testingpractice.duoclonebackend.auth.GoogleOAuthClient;
    import com.testingpractice.duoclonebackend.auth.GoogleTokenResponse;
    import com.testingpractice.duoclonebackend.dto.GoogleUserInfo;
    import com.testingpractice.duoclonebackend.dto.UserDto;
    import com.testingpractice.duoclonebackend.entity.User;
    import com.testingpractice.duoclonebackend.exception.ApiException;
    import com.testingpractice.duoclonebackend.exception.ErrorCode;
    import com.testingpractice.duoclonebackend.mapper.UserMapper;
    import com.testingpractice.duoclonebackend.repository.UserRepository;
    import jakarta.servlet.http.Cookie;
    import jakarta.servlet.http.HttpServletResponse;
    import jakarta.transaction.Transactional;
    import java.util.Map;
    import lombok.RequiredArgsConstructor;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.http.HttpEntity;
    import org.springframework.http.HttpHeaders;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.MediaType;
    import org.springframework.stereotype.Service;
    import org.springframework.util.LinkedMultiValueMap;
    import org.springframework.util.MultiValueMap;
    import org.springframework.web.client.RestTemplate;
    import org.springframework.web.server.ResponseStatusException;

    @Service
    @RequiredArgsConstructor
    public class GoogleServiceImpl implements GoogleService {

        private final GoogleOAuthClient oauth;          // NEW
        private final UserRepository userRepository;
        private final UserMapper userMapper;
        private final JwtServiceImpl jwtService;
        private final UserCreationService userCreationService;
        private final AuthCookieService authCookieService; // NEW

        private static final int DAY = 24 * 60 * 60;

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

        public UserDto getCurrentUser(String token) {
            Integer userId = jwtService.extractUserId(token);
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
            return userMapper.toDto(user);
        }

    }