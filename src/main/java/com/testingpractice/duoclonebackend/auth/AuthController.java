package com.testingpractice.duoclonebackend.auth;

import com.testingpractice.duoclonebackend.constants.pathConstants;
import com.testingpractice.duoclonebackend.dto.UserDto;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(pathConstants.AUTH)
public class AuthController {

    private final GoogleService googleService;

    @PostMapping(pathConstants.GOOGLE_LOGIN)
    public ResponseEntity<UserDto> loginWithGoogle(@RequestBody TokenDto dto, HttpServletResponse response) {
        UserDto userDto = googleService.loginOrRegister(dto.getAccessToken(), response);
        return ResponseEntity.ok(userDto);
    }


}
