package com.kakao.payment.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kakao.payment.controller.dto.response.ApiErrorResponse;
import com.kakao.payment.controller.dto.response.ApiResponse;
import com.kakao.payment.controller.dto.response.PaymentCreationResponse;
import com.kakao.payment.controller.dto.response.PaymentFetchResponse;
import com.kakao.payment.controller.dto.response.PaymentGetResponse;
import com.kakao.payment.persistence.entity.Payment;
import com.kakao.payment.persistence.entity.PaymentDistribution;
import com.kakao.payment.persistence.repository.PaymentDistributionRepository;
import com.kakao.payment.persistence.repository.PaymentRepository;

@Service
public class PaymentService {

	@Autowired
	private PaymentRepository paymentRepository;

	@Autowired
	private PaymentDistributionRepository distributionRepository;

	@Transactional
	public ApiResponse create(int userId, String roomId, int amount, int userCount) {
		Payment payment = new Payment();

		// from request
		payment.setUserId(userId);
		payment.setRoomId(roomId);
		payment.setAmount(amount);
		payment.setUserCount(userCount);

		// time
		LocalDateTime currentDateTime = LocalDateTime.now();
		payment.setCreatedAt(currentDateTime);
		payment.setExpiredAt(currentDateTime.plusMinutes(10));

		// token
		String token = generateSimpleToken();
		payment.setToken(token);

		// 중복 token 인 경우 에러 (중복이 없다고 가정)
		if(paymentRepository.findByToken(token) != null) {
			return new ApiErrorResponse(104, "temporarily unavailable, please try again", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		// id
		UUID paymentId = UUID.randomUUID();
		payment.setPaymentId(paymentId.toString());

		paymentRepository.save(payment);

		// 유저당 금액 계산 (소수점 이하 남는 금액은 모아서 한사람에게)
		int amountPerUser = amount / userCount;
		int specialAmount = amount - (amountPerUser * (userCount - 1));

		for (int i = 0; i < userCount; i++) {
			PaymentDistribution distribution = new PaymentDistribution();
			distribution.setAssignedUserId(null);
			distribution.setPaymentId(paymentId.toString());
			if (i == userCount - 1) {
				distribution.setAmount(specialAmount);
			} else {
				distribution.setAmount(amountPerUser);
			}
			distributionRepository.save(distribution);
		}

		return new PaymentCreationResponse(token);
	}

	@Transactional
	public ApiResponse fetch(String token, int userId, String roomId) {

		// 잘못된 토큰
		Payment payment = paymentRepository.findByToken(token);
		if (payment == null) {
			return new ApiErrorResponse(201, "Not found", HttpStatus.NOT_FOUND);
		}

		// 자기 자신은 못받음
		if (payment.getUserId() == userId) {
			return new ApiErrorResponse(202, "Can't get money created by yourself", HttpStatus.FORBIDDEN);
		}

		// 방 번호 다르면 못받음
		if (payment.getRoomId().equals(roomId) == false) {
			return new ApiErrorResponse(203, "Wrong room id", HttpStatus.FORBIDDEN);
		}

		// 시간 지나면 못받음
		LocalDateTime currentDateTime = LocalDateTime.now();
		if (currentDateTime.isAfter(payment.getExpiredAt()) == true) {
			return new ApiErrorResponse(204, "Expired", HttpStatus.FORBIDDEN);
		}
		
		// 한번만 받을 수 있음
		if(distributionRepository.findAssignmentByPaymentIdAndUserId(payment.getPaymentId(), userId) != null) {
			return new ApiErrorResponse(205, "You have already received", HttpStatus.FORBIDDEN);
		}
		
		PaymentDistribution distribution = distributionRepository.findAvailable(payment.getPaymentId());

		// 모두 할당됨
		if (distribution == null) {
			return new ApiErrorResponse(205, "Already consumed", HttpStatus.NOT_FOUND);
		}

		// 유저에게 할당됨
		distribution.setAssignedUserId(userId);

		distributionRepository.save(distribution);

		return new PaymentFetchResponse(distribution.getAmount());
	}

	public ApiResponse get(String token, int userId) {
		Payment payment = paymentRepository.findByToken(token);

		// 잘못된 토큰
		if (payment == null) {
			return new ApiErrorResponse(301, "Not found", HttpStatus.NOT_FOUND);
		}
		
		// 내꺼 아님
		if(payment.getUserId() != userId) {
			return new ApiErrorResponse(302, "It's not yours", HttpStatus.FORBIDDEN);
		}

		List<PaymentDistribution> distributions = distributionRepository
				.findAllAssignmentdByPaymentId(payment.getPaymentId());
		Map<Integer, Integer> consumedList = new HashMap<Integer, Integer>();

		int consumed = 0;
		for (int i = 0; i < distributions.size(); i++) {
			PaymentDistribution distribution = distributions.get(i);
			consumedList.put(distribution.getAssignedUserId(), distribution.getAmount());
			consumed += distribution.getAmount();
		}

		return new PaymentGetResponse(payment.getCreatedAt(), payment.getAmount(), consumed, consumedList);
	}

	public String generateSimpleToken() {
		Random rand = new Random();

		char[] c = new char[3];
		for (int i = 0; i < 3; i++) {
			int type = rand.nextInt() % 3; // 0 : number, 1 : alphabet
			if (type == 0) {
				c[i] = (char) (rand.nextInt(10) + '0');
			} else {
				c[i] = (char) (rand.nextInt(26) + 'a');
			}
		}

		return String.valueOf(c);
	}

}
