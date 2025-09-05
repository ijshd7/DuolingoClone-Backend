package com.testingpractice.duoclonebackend.exception;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {
  private final ErrorCode code;
  private final HttpStatus status;

  public ApiException(ErrorCode code, HttpStatus status) {
    super(code.defaultMessage());
    this.code = code;
    this.status = status;
  }

  public ApiException(ErrorCode code, HttpStatus status, String overrideMessage) {
    super(overrideMessage);
    this.code = code;
    this.status = status;
  }

  public ErrorCode code() {
    return code;
  }

  public HttpStatus status() {
    return status;
  }
}
