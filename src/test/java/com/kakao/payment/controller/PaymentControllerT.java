package com.kakao.payment.controller;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.kakao.payment.controller.dto.request.PaymentCreationRequest;
import com.kakao.payment.persistence.entity.Payment;
import com.kakao.payment.persistence.repository.PaymentRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PaymentControllerT {

	@Autowired
	private WebTestClient webClient;

	@Autowired
	private PaymentRepository paymentRepository;

	@Test
	public void _A1_create() {

		// wrong amount
		webClient.post().uri("/api/payment").header("X-ROOM-ID", "2").header("X-USER-ID", "1")
				.bodyValue(new PaymentCreationRequest(0, 1)).exchange().expectStatus().is4xxClientError().expectBody()
				.jsonPath("errorCode").isEqualTo("101");

		webClient.post().uri("/api/payment").header("X-ROOM-ID", "2").header("X-USER-ID", "1")
				.bodyValue(new PaymentCreationRequest(0, 10000001)).exchange().expectStatus().is4xxClientError()
				.expectBody().jsonPath("errorCode").isEqualTo("101");

		// wrong user count
		webClient.post().uri("/api/payment").header("X-ROOM-ID", "2").header("X-USER-ID", "1")
				.bodyValue(new PaymentCreationRequest(1, 0)).exchange().expectStatus().is4xxClientError().expectBody()
				.jsonPath("errorCode").isEqualTo("102");

		webClient.post().uri("/api/payment").header("X-ROOM-ID", "2").header("X-USER-ID", "1")
				.bodyValue(new PaymentCreationRequest(1, 101)).exchange().expectStatus().is4xxClientError().expectBody()
				.jsonPath("errorCode").isEqualTo("102");

		// amount < user count
		webClient.post().uri("/api/payment").header("X-ROOM-ID", "2").header("X-USER-ID", "1")
				.bodyValue(new PaymentCreationRequest(3, 10)).exchange().expectStatus().is4xxClientError().expectBody()
				.jsonPath("errorCode").isEqualTo("103");

		// success case
		EntityExchangeResult<byte[]> result = webClient.post().uri("/api/payment").header("X-ROOM-ID", "2")
				.header("X-USER-ID", "1").bodyValue(new PaymentCreationRequest(1000, 3)).exchange().expectStatus()
				.is2xxSuccessful().expectBody().jsonPath("result").isEqualTo("true").returnResult();

		String resultText = result.toString();
		int startIdx = resultText.indexOf("token\":");
		String token = resultText.substring(startIdx + 8, startIdx + 11);

		paymentRepository.deleteByToken(token);

	}

	@Test
	public void _B1_fetch() {

		// get token
		EntityExchangeResult<byte[]> result = webClient.post().uri("/api/payment").header("X-ROOM-ID", "2")
				.header("X-USER-ID", "1").bodyValue(new PaymentCreationRequest(1000, 2)).exchange().expectStatus()
				.is2xxSuccessful().expectBody().jsonPath("result").isEqualTo("true").returnResult();

		String resultText = result.toString();
		int startIdx = resultText.indexOf("token\":");
		String token = resultText.substring(startIdx + 8, startIdx + 11);

		// 잘못된 토큰
		webClient.get().uri(uriBuilder -> uriBuilder.path("/api/payment/fetch").queryParam("token", "-13").build())
				.header("X-ROOM-ID", "2").header("X-USER-ID", "1").exchange().expectStatus().is4xxClientError()
				.expectBody().jsonPath("errorCode").isEqualTo("201");

		// 자기 자신은 못받음
		webClient.get().uri(uriBuilder -> uriBuilder.path("/api/payment/fetch").queryParam("token", token).build())
				.header("X-ROOM-ID", "2").header("X-USER-ID", "1").exchange().expectStatus().is4xxClientError()
				.expectBody().jsonPath("errorCode").isEqualTo("202");

		// 방 번호 다르면 못받음
		webClient.get().uri(uriBuilder -> uriBuilder.path("/api/payment/fetch").queryParam("token", token).build())
				.header("X-ROOM-ID", "3").header("X-USER-ID", "2").exchange().expectStatus().is4xxClientError()
				.expectBody().jsonPath("errorCode").isEqualTo("203");

		// 성공
		webClient.get().uri(uriBuilder -> uriBuilder.path("/api/payment/fetch").queryParam("token", token).build())
				.header("X-ROOM-ID", "2").header("X-USER-ID", "2").exchange().expectStatus().is2xxSuccessful()
				.expectBody().jsonPath("result").isEqualTo("true");

		// 한번만 받을 수 있음
		webClient.get().uri(uriBuilder -> uriBuilder.path("/api/payment/fetch").queryParam("token", token).build())
				.header("X-ROOM-ID", "2").header("X-USER-ID", "2").exchange().expectStatus().is4xxClientError()
				.expectBody().jsonPath("errorCode").isEqualTo("205");

		// 모두 할당된 경우
		webClient.get().uri(uriBuilder -> uriBuilder.path("/api/payment/fetch").queryParam("token", token).build())
				.header("X-ROOM-ID", "2").header("X-USER-ID", "3").exchange().expectStatus().is2xxSuccessful()
				.expectBody().jsonPath("result").isEqualTo("true");

		webClient.get().uri(uriBuilder -> uriBuilder.path("/api/payment/fetch").queryParam("token", token).build())
				.header("X-ROOM-ID", "2").header("X-USER-ID", "4").exchange().expectStatus().is4xxClientError()
				.expectBody().jsonPath("errorCode").isEqualTo("205");

		paymentRepository.deleteByToken(token);
	}

	@Test
	public void _B2_fetch() {
		// 만료된 토큰 삽입
		Payment payment = new Payment();

		payment.setUserId(1);
		payment.setRoomId("2");
		payment.setAmount(1000);
		payment.setUserCount(1);

		// time
		LocalDateTime currentDateTime = LocalDateTime.now();
		payment.setCreatedAt(currentDateTime.minusMinutes(11));
		payment.setExpiredAt(currentDateTime.minusMinutes(21));

		// token
		String token = "-12";
		payment.setToken(token);

		// id
		UUID paymentId = UUID.randomUUID();
		payment.setPaymentId(paymentId.toString());

		paymentRepository.save(payment);

		// 시간 지나면 못받음
		webClient.get().uri(uriBuilder -> uriBuilder.path("/api/payment/fetch").queryParam("token", token).build())
				.header("X-ROOM-ID", "2").header("X-USER-ID", "4").exchange().expectStatus().is4xxClientError()
				.expectBody().jsonPath("errorCode").isEqualTo("204");

		paymentRepository.delete(payment);
	}

	@Test
	public void _C1_get() {

		// get token
		EntityExchangeResult<byte[]> result = webClient.post().uri("/api/payment").header("X-ROOM-ID", "2")
				.header("X-USER-ID", "1").bodyValue(new PaymentCreationRequest(1000, 2)).exchange().expectStatus()
				.is2xxSuccessful().expectBody().jsonPath("result").isEqualTo("true").returnResult();

		String resultText = result.toString();
		int startIdx = resultText.indexOf("token\":");
		String token = resultText.substring(startIdx + 8, startIdx + 11);

		// 잘못된 토큰
		webClient.get().uri(uriBuilder -> uriBuilder.path("/api/payment").queryParam("token", "-13").build())
				.header("X-ROOM-ID", "2").header("X-USER-ID", "1").exchange().expectStatus().is4xxClientError()
				.expectBody().jsonPath("errorCode").isEqualTo("301");

		// 내꺼 아님
		webClient.get().uri(uriBuilder -> uriBuilder.path("/api/payment").queryParam("token", token).build())
				.header("X-ROOM-ID", "2").header("X-USER-ID", "3").exchange().expectStatus().is4xxClientError()
				.expectBody().jsonPath("errorCode").isEqualTo("302");

		// 성공
		webClient.get().uri(uriBuilder -> uriBuilder.path("/api/payment").queryParam("token", token).build())
				.header("X-ROOM-ID", "2").header("X-USER-ID", "1").exchange().expectStatus().is2xxSuccessful()
				.expectBody().jsonPath("result").isEqualTo("true");

		paymentRepository.deleteByToken(token);

	}
}
