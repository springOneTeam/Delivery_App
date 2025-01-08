package com.example.outsourcingproject.exception.auth;

import com.example.outsourcingproject.exception.ErrorCode;
import com.example.outsourcingproject.exception.common.BusinessException;

public class WrongCredentialsException extends BusinessException {
    public WrongCredentialsException() {
        super(ErrorCode.WRONG_CREDENTIALS);
    }

    public WrongCredentialsException(String detail) {
        super(ErrorCode.WRONG_CREDENTIALS, detail);
    }
}