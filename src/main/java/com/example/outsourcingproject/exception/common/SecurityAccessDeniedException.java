package com.example.outsourcingproject.exception.common;

import com.example.outsourcingproject.exception.ErrorCode;

public class SecurityAccessDeniedException extends BusinessException {

	public SecurityAccessDeniedException() {
		super(ErrorCode.FORBIDDEN_ACCESS);
	}

	public SecurityAccessDeniedException(String detail) {
		super(ErrorCode.FORBIDDEN_ACCESS, detail);
	}

	public SecurityAccessDeniedException(ErrorCode errorCode) {
		super(errorCode);
	}
}
