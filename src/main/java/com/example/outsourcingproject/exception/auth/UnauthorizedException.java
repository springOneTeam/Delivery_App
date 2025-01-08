package com.example.outsourcingproject.exception.auth;

import com.example.outsourcingproject.exception.ErrorCode;
import com.example.outsourcingproject.exception.common.BusinessException;

public class UnauthorizedException extends BusinessException {
    public UnauthorizedException() {
        super(ErrorCode.UNAUTHORIZED_ACCESS);
    }

    public UnauthorizedException(String detail) {
        super(ErrorCode.UNAUTHORIZED_ACCESS, detail);
    }
}