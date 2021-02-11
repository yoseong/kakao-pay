package com.kakao.payment.controller.dto.request;

public class PaymentCreationRequest {

	private int amount;

	private int userCount;

	public PaymentCreationRequest(int amount, int count) {
		this.amount = amount;
		this.userCount = count;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getUserCount() {
		return userCount;
	}

	public void setUserCount(int userCount) {
		this.userCount = userCount;
	}

}
