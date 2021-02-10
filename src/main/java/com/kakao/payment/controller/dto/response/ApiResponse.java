package com.kakao.payment.controller.dto.response;

import org.springframework.http.HttpStatus;

public class ApiResponse {
	private boolean result;
	private HttpStatus httpStatus;

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}
}
