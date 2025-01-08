package com.example.outsourcingproject.exception.common;

import com.example.outsourcingproject.exception.ErrorCode;

public class UserNotFoundException extends BusinessException {
    public UserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND);
    }

    public UserNotFoundException(String detail) {
        super(ErrorCode.USER_NOT_FOUND, detail);
    }
}