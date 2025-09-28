package com.testingpractice.duoclonebackend.auth;

import com.testingpractice.duoclonebackend.dto.UserDto;
import com.testingpractice.duoclonebackend.entity.User;
import com.testingpractice.duoclonebackend.mapper.UserMapper;
import com.testingpractice.duoclonebackend.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GoogleService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtService jwtService;

    @Value("${google.client-id}")
    private String clientId;

    @Value("${google.client-secret}")
    private String clientSecret;

    @Transactional
    public UserDto loginOrRegisterWithCode(String code, HttpServletResponse response) {
        RestTemplate rest = new RestTemplate();

        // Step 1: Exchange auth code for access token
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", "postmessage"); // use "postmessage" for Google JS auth
        params.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(params, headers);

        Map<String, Object> tokenResponse = rest.postForObject(
                "https://oauth2.googleapis.com/token",
                tokenRequest,
                Map.class
        );

        String accessToken = (String) tokenResponse.get("access_token");

        // Step 2: Fetch user info
        String url = "https://www.googleapis.com/oauth2/v3/userinfo?access_token=" + accessToken;
        GoogleUserInfo googleUser = rest.getForObject(url, GoogleUserInfo.class);

        // Step 3: Register or fetch user
        User user = (User) userRepository.findByEmail(googleUser.getEmail())
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(googleUser.getEmail());
                    newUser.setFirstName(googleUser.getGivenName());
                    newUser.setLastName(googleUser.getFamilyName());
                    newUser.setUsername(googleUser.getName().toLowerCase(Locale.ROOT));
                    newUser.setPoints(0);
                    newUser.setStreakLength(0);
                    newUser.setCreatedAt(Timestamp.from(Instant.now()));
                    return userRepository.save(newUser);
                });

        // Step 4: Create JWT cookie
        String jwt = jwtService.createToken(user.getId());
        Cookie cookie = new Cookie("jwt", jwt);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60);
        response.addCookie(cookie);

        return userMapper.toDto(user);
    }

    @Data
    public static class GoogleUserInfo {
        private String email;
        private String name;
        private String givenName;
        private String familyName;
    }
}