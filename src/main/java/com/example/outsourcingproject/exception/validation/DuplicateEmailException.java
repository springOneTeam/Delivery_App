package com.example.outsourcingproject.exception.validation;

import com.example.outsourcingproject.exception.ErrorCode;
import com.example.outsourcingproject.exception.common.BusinessException;

import lombok.Getter;

@Getter
public class DuplicateEmailException extends BusinessException {
    public DuplicateEmailException() {
        super(ErrorCode.DUPLICATE_EMAIL);
    }

    public DuplicateEmailException(String detail) {
        super(ErrorCode.DUPLICATE_EMAIL, detail);
    }
}