package com.testingpractice.duoclonebackend.dto;

import java.sql.Timestamp;

public record UserDto (

    Integer id,
    String username,
    String firstName,
    String lastName,
    String pfpSrc,
    Integer pointer,
    Timestamp createdAt
) {}
