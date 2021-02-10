package com.kakao.payment.controller.dto.response;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.HttpStatus;

public class PaymentGetResponse extends ApiResponse {

	private LocalDateTime createdAt;
	private int amount;
	private int consumed;
	private Map<Integer, Integer> consumedList;

	public PaymentGetResponse(LocalDateTime createdAt, int amount, int consumed, Map<Integer, Integer> consumedList) {
		super.setHttpStatus(HttpStatus.OK);
		super.setResult(true);
		
		this.createdAt = createdAt;
		this.amount = amount;
		this.consumed = consumed;
		this.consumedList = consumedList;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getConsumed() {
		return consumed;
	}

	public void setConsumed(int consumed) {
		this.consumed = consumed;
	}

	public Map<Integer, Integer> getConsumedList() {
		return consumedList;
	}

	public void setConsumedList(Map<Integer, Integer> consumedList) {
		this.consumedList = consumedList;
	}
}
