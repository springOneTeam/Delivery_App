package com.example.outsourcingproject.exception.auth;

import com.example.outsourcingproject.exception.ErrorCode;
import com.example.outsourcingproject.exception.common.BusinessException;

public class LoginFailedException extends BusinessException {
    public LoginFailedException() {
        super(ErrorCode.LOGIN_FAILED);
    }

    public LoginFailedException(String detail) {
        super(ErrorCode.LOGIN_FAILED, detail);
    }
}