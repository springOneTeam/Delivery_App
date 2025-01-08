package com.example.outsourcingproject.exception.common;

import com.example.outsourcingproject.exception.ErrorCode;

public class NotFoundException extends BusinessException {
    public NotFoundException() {
        super(ErrorCode.RESOURCE_NOT_FOUND);
    }

    public NotFoundException(String detail) {
        super(ErrorCode.RESOURCE_NOT_FOUND, detail);
    }
}