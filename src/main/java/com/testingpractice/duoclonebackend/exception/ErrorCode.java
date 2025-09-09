package com.testingpractice.duoclonebackend.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"),
  LESSON_NOT_FOUND(HttpStatus.NOT_FOUND, "Lesson not found"),
  UNIT_NOT_FOUND(HttpStatus.NOT_FOUND, "Unit not found"),
  SECTION_NOT_FOUND(HttpStatus.NOT_FOUND, "Section not found"),
  PROGRESS_NOT_FOUND(HttpStatus.NOT_FOUND, "User progress not found"),
  COURSE_END(HttpStatus.BAD_REQUEST, "No next lesson — course complete"),
  OPTION_NOT_FOUND(HttpStatus.NOT_FOUND, "No option found for the submitted option");

  private final HttpStatus status;
  private final String defaultMessage;

  ErrorCode(HttpStatus status, String msg) {
    this.status = status;
    this.defaultMessage = msg;
  }

  public HttpStatus status() {
    return status;
  }

  public String defaultMessage() {
    return defaultMessage;
  }
}
