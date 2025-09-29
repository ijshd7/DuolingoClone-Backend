package com.testingpractice.duoclonebackend.exception;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {
  private final ErrorCode code;

  public ApiException(ErrorCode code) {
    super(code.defaultMessage());
    this.code = code;
  }

  public ApiException(ErrorCode code, String overrideMessage) {
    super(overrideMessage);
    this.code = code;
  }

  public ErrorCode code() {
    return code;
  }

  public HttpStatus status() {
    return code.status();
  }
}
