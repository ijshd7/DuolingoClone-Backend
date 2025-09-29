package com.testingpractice.duoclonebackend.service;

import jakarta.servlet.http.HttpServletRequest;

public interface JwtService {

    String createToken(Integer userId);

    Integer extractUserId(String token);

    int requireUserId(String token);

    int getUserIdFromaRequest(HttpServletRequest authorizationHeader);

}
