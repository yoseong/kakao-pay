package com.kakao.payment.controller.dto.response;

import org.springframework.http.HttpStatus;

public class ApiErrorResponse extends ApiResponse {

	private int errorCode;
	private String description;

	public ApiErrorResponse(int errorCode, String description, HttpStatus httpStatus) {
		super.setResult(false);;
		super.setHttpStatus(httpStatus);
		this.errorCode = errorCode;
		this.description = description;
	}


	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
