package com.example.outsourcingproject.exception.common;

import com.example.outsourcingproject.exception.ErrorCode;

public class OrderNotFoundException extends BusinessException {
  public OrderNotFoundException(){super(ErrorCode.RESOURCE_NOT_FOUND);
  }

  public OrderNotFoundException(String detail) {
    super(ErrorCode.RESOURCE_NOT_FOUND, detail);
  }

  public OrderNotFoundException(ErrorCode errorCode) {
    super(errorCode);
  }
}
