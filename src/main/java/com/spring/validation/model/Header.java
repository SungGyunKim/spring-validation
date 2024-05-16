package com.spring.validation.model;

import lombok.Builder;

@Builder
public class Header {
  boolean isSuccessful;
  int resultCode;
  String resultMessage;
}
