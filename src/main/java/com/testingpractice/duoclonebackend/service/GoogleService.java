package com.testingpractice.duoclonebackend.service;

import com.testingpractice.duoclonebackend.dto.UserDto;
import jakarta.servlet.http.HttpServletResponse;

public interface GoogleService {

    UserDto loginOrRegisterWithCode(String code, HttpServletResponse response);

}

