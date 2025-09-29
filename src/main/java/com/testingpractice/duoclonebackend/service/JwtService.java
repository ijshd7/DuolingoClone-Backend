package com.testingpractice.duoclonebackend.service;

public interface JwtService {

    String createToken(Integer userId);

    Integer extractUserId(String token);

    int requireUserId(String token);

}
