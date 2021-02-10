package com.kakao.payment.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "payment_distribution")
public class PaymentDistribution {

	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid2")
	@GeneratedValue(generator = "system-uuid")
	@Column(name = "distribution_id", length = 36)
	private String distributionId;

	@Column(name = "payment_id", length = 36)
	private String paymentId;

	@Column(name = "assigned_user_id")
	private Integer assignedUserId;

	@Column(name = "amount")
	private Integer amount;

	public String getDistributionId() {
		return distributionId;
	}

	public void setDistributionId(String distributionId) {
		this.distributionId = distributionId;
	}

	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public Integer getAssignedUserId() {
		return assignedUserId;
	}

	public void setAssignedUserId(Integer assignedUserId) {
		this.assignedUserId = assignedUserId;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}
}
