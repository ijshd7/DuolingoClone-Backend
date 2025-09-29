package com.testingpractice.duoclonebackend.service;

import jakarta.servlet.http.HttpServletRequest;

public interface JwtService {

    String createToken(Integer userId);

    int requireUserId(String token);

}
