package com.testingpractice.duoclonebackend.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(ApiException.class)
  public ResponseEntity<ProblemDetail> handleApi(ApiException ex) {
    ProblemDetail pd = ProblemDetail.forStatus(ex.status());
    pd.setTitle(ex.code().name());
    pd.setDetail(ex.getMessage());
    pd.setProperty("code", ex.code().name());
    return ResponseEntity.status(ex.status()).body(pd);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ProblemDetail> handleAny(Exception ex, HttpServletRequest req) {
    ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    log.error(
        "Unhandled {} {} ", req.getMethod(), req.getRequestURI(), ex); // <-- prints stacktrace

    pd.setTitle("INTERNAL_ERROR");
    pd.setDetail("Unexpected error");
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(pd);
  }
}
