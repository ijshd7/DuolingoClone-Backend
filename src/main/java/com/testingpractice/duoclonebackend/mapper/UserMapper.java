package com.testingpractice.duoclonebackend.mapper;

import com.testingpractice.duoclonebackend.dto.UserDto;
import com.testingpractice.duoclonebackend.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "streakLength", expression = "java(streakLength)")
    UserDto toDto(User user, Integer streakLength);

}
