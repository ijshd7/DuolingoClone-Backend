package com.testingpractice.duoclonebackend.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenDto {
  private String code;
}
