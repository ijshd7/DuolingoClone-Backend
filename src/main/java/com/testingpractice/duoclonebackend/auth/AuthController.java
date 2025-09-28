package com.testingpractice.duoclonebackend.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/google-login")
    public ResponseEntity<?> loginWithGoogle(@RequestBody TokenDto dto, HttpServletResponse response) {
        User user = googleService.loginOrRegister(dto.getToken());
        String jwt = jwtService.createToken(user.getId());

        Cookie cookie = new Cookie("jwt", jwt);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60); // 1 day
        // cookie.setSecure(true); // enable in prod with HTTPS
        response.addCookie(cookie);

        return ResponseEntity.ok(Map.of("user", user));
    }


}
