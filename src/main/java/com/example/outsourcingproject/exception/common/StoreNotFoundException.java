package com.example.outsourcingproject.exception.common;

import com.example.outsourcingproject.exception.ErrorCode;

public class StoreNotFoundException extends BusinessException{

	public StoreNotFoundException(){
		super(ErrorCode.RESOURCE_NOT_FOUND);
	}

	public StoreNotFoundException(String detail) {
		super(ErrorCode.RESOURCE_NOT_FOUND, detail);
	}
}
