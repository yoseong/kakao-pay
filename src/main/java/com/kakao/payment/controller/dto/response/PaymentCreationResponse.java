package com.kakao.payment.controller.dto.response;

import org.springframework.http.HttpStatus;

public class PaymentCreationResponse extends ApiResponse {

	private String token;

	public PaymentCreationResponse(String token) {
		super.setResult(true);
		super.setHttpStatus(HttpStatus.CREATED);
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
