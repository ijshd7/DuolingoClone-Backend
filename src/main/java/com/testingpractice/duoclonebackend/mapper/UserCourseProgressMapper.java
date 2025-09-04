package com.testingpractice.duoclonebackend.mapper;

import com.testingpractice.duoclonebackend.dto.SectionDto;
import com.testingpractice.duoclonebackend.dto.UserCourseProgressDto;
import com.testingpractice.duoclonebackend.entity.Section;
import com.testingpractice.duoclonebackend.entity.UserCourseProgress;
import org.mapstruct.Mapper;

import java.util.List;
@Mapper(componentModel = "spring")
public interface UserCourseProgressMapper {

    UserCourseProgressDto toDto (UserCourseProgress userCourseProgress);

}
