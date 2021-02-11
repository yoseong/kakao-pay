package com.kakao.payment.persistence.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "payment", indexes = @Index(name ="token_index", columnList = "token"))
public class Payment {
	@Id
	@Column(name = "payment_id", length = 36)
	private String paymentId;

	@Column(name = "user_id")
	private int userId;

	@Column(name = "room_id", length = 36)
	private String roomId;

	@Column(name = "token", length = 3)
	private String token;

	@Column(name = "amount")
	private int amount;

	@Column(name = "user_count")
	private int userCount;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "expired_at")
	private LocalDateTime expiredAt;

	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
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

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getExpiredAt() {
		return expiredAt;
	}

	public void setExpiredAt(LocalDateTime expiredAt) {
		this.expiredAt = expiredAt;
	}
}
