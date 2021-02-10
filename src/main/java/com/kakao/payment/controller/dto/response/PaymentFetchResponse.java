package com.kakao.payment.controller.dto.response;

import org.springframework.http.HttpStatus;

public class PaymentFetchResponse extends ApiResponse {
	private int amount;

	public PaymentFetchResponse(int amount) {
		super.setResult(true);
		super.setHttpStatus(HttpStatus.OK);
		this.amount = amount;
	}
	
	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
}
