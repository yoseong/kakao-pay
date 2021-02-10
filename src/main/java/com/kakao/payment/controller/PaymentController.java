package com.kakao.payment.controller;

import org.springframework.web.bind.annotation.RestController;

import com.kakao.payment.controller.dto.request.PaymentCreationRequest;
import com.kakao.payment.controller.dto.response.ApiErrorResponse;
import com.kakao.payment.controller.dto.response.ApiResponse;
import com.kakao.payment.service.PaymentService;

import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/")
public class PaymentController {
	
	@Autowired
	private PaymentService paymentService;

	@PostMapping("/payment")
	@ApiOperation(value = "돈 뿌리기", tags = "payment-controller")
	public ResponseEntity<ApiResponse> create(@RequestHeader(value = "X-USER-ID") int xUserId,
			@RequestHeader(value = "X-ROOM-ID") String xRoomId, @RequestBody PaymentCreationRequest request) {

		ApiResponse response = null;

		// parameter validation
		int amount = request.getAmount();
		if (amount <= 0 || amount > 10000000) {
			response = new ApiErrorResponse(101, "amount must be 1 ~ 10000000", HttpStatus.FORBIDDEN);
			return new ResponseEntity<ApiResponse>(response, response.getHttpStatus());
		}

		int userCount = request.getUserCount();
		if (userCount <= 0 || userCount > 100) {
			response = new ApiErrorResponse(102, "user count must be 1 ~ 100", HttpStatus.FORBIDDEN);
			return new ResponseEntity<ApiResponse>(response, response.getHttpStatus());
		}

		if (amount < userCount) {
			response = new ApiErrorResponse(103, "price must be larger than user count", HttpStatus.FORBIDDEN);
			return new ResponseEntity<ApiResponse>(response, response.getHttpStatus());
		}

		response = paymentService.create(xUserId, xRoomId, request.getAmount(), request.getUserCount());

		return new ResponseEntity<ApiResponse>(response, response.getHttpStatus());
	}

	@GetMapping("/payment/fetch")
	@ApiOperation(value = "뿌린 돈 받기", tags = "payment-controller")
	public ResponseEntity<ApiResponse> fetch(@RequestHeader(value = "X-USER-ID") int xUserId,
			@RequestHeader(value = "X-ROOM-ID") String xRoomId,
			@RequestParam(name = "token", required = true) String token) {

		ApiResponse response = paymentService.fetch(token, xUserId, xRoomId);
		
		return new ResponseEntity<ApiResponse>(response, response.getHttpStatus());
	}
	
	@GetMapping("/payment")
	@ApiOperation(value = "내가 뿌린 돈 조회", tags = "payment-controller")
	public ResponseEntity<ApiResponse> get(@RequestHeader(value = "X-USER-ID") int xUserId,
			@RequestHeader(value = "X-ROOM-ID") String xRoomId,
			@RequestParam(name = "token", required = true) String token) {

		ApiResponse response = paymentService.get(token, xUserId);
		
		return new ResponseEntity<ApiResponse>(response, response.getHttpStatus());
	}
}
