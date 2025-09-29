package com.testingpractice.duoclonebackend.mapper;

import com.testingpractice.duoclonebackend.dto.UserCourseProgressDto;
import com.testingpractice.duoclonebackend.entity.UserCourseProgress;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserCourseProgressMapper {

  @Mapping(target = "completedLessons", expression = "java(completedLessons)")
  @Mapping(target = "sectionId", expression = "java(sectionId)")
  UserCourseProgressDto toDto(
      UserCourseProgress entity, Integer completedLessons, Integer sectionId);
}
