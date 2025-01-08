package com.example.outsourcingproject.exception.validation;

import com.example.outsourcingproject.exception.ErrorCode;
import com.example.outsourcingproject.exception.common.BusinessException;

public class WrongAccessException extends BusinessException {
    public WrongAccessException() {
        super(ErrorCode.INVALID_ACCESS);
    }

    public WrongAccessException(String detail) {
        super(ErrorCode.INVALID_ACCESS, detail);
    }
}