package com.kakao.payment.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.kakao.payment.persistence.entity.PaymentDistribution;

@Repository
public interface PaymentDistributionRepository extends JpaRepository<PaymentDistribution, String> {

	@Query(value = "select d from PaymentDistribution as d where paymentId=?1 and assignedUserId=?2")
	PaymentDistribution findAssignmentByPaymentIdAndUserId(String paymentId, int userId);
	
	@Query(value = "select * from payment_distribution where payment_id=?1 and assigned_user_id is null "
			+ "order by amount desc limit 1", nativeQuery = true)
	PaymentDistribution findAvailable(String paymentId);
	
	@Query(value = "select d from PaymentDistribution as d where paymentId=?1 and assignedUserId is not null")
	List<PaymentDistribution> findAllAssignmentdByPaymentId(String paymentId); 
	
}
