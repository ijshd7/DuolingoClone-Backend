package com.testingpractice.duoclonebackend.exception;

public enum ErrorCode {
  USER_NOT_FOUND("User not found"),
  LESSON_NOT_FOUND("Lesson not found"),
  UNIT_NOT_FOUND("Unit not found"),
  SECTION_NOT_FOUND("Section not found"),
  PROGRESS_NOT_FOUND("User progress not found"),
  COURSE_END("No next lesson — course complete");

  private final String defaultMessage;

  ErrorCode(String msg) {
    this.defaultMessage = msg;
  }

  public String defaultMessage() {
    return defaultMessage;
  }
}
