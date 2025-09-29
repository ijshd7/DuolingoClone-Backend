package com.testingpractice.duoclonebackend.service;


public interface JwtService {

    String createToken(Integer userId);

    int requireUserId(String token);

}
