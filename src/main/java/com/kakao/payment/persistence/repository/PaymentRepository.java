package com.kakao.payment.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kakao.payment.persistence.entity.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {

	@Query(value = "select p from Payment as p where token=?1")
	Payment findByToken(String token);
	
	// for unit test
	@Transactional
	@Modifying
	@Query(value = "delete from Payment where token=?1")
	void deleteByToken(String token);
	
}
