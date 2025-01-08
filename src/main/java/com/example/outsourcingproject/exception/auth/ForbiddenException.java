package com.example.outsourcingproject.exception.auth;

import com.example.outsourcingproject.exception.ErrorCode;
import com.example.outsourcingproject.exception.common.BusinessException;

public class ForbiddenException extends BusinessException {
    public ForbiddenException() {
        super(ErrorCode.FORBIDDEN_ACCESS);
    }

    public ForbiddenException(String detail) {
        super(ErrorCode.FORBIDDEN_ACCESS, detail);
    }
}