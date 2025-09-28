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
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class GoogleService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtService jwtService;

    @Transactional
    public UserDto loginOrRegister(String accessToken, HttpServletResponse response) {
        RestTemplate rest = new RestTemplate();
        String url = "https://www.googleapis.com/oauth2/v3/userinfo?access_token=" + accessToken;
        GoogleUserInfo googleUser = rest.getForObject(url, GoogleUserInfo.class);

        User user = (User) userRepository.findByEmail(googleUser.getEmail())
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(googleUser.getEmail());
                    newUser.setFirstName(googleUser.getGivenName());
                    newUser.setLastName(googleUser.getFamilyName());
                    newUser.setUsername(googleUser.getName().toLowerCase(Locale.ROOT));
                    return userRepository.save(newUser);
                });

        // Create JWT and set as http-only cookie
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