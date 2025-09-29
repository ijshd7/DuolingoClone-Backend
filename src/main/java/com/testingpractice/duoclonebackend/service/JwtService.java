package com.testingpractice.duoclonebackend.service;

public interface JwtService {

    String createToken(Integer userId);

    boolean isTokenValid(String token);

    Integer extractUserId(String token);

}
