package com.example.outsourcingproject.exception.validation;

import com.example.outsourcingproject.exception.ErrorCode;
import com.example.outsourcingproject.exception.common.BusinessException;

import lombok.Getter;

@Getter
public class DuplicateUsernameException extends BusinessException {
    public DuplicateUsernameException() {
        super(ErrorCode.DUPLICATE_USERNAME);
    }

    public DuplicateUsernameException(String detail) {
        super(ErrorCode.DUPLICATE_USERNAME, detail);
    }
}