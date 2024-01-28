package com.clova.anifriends.domain.payment.repository;

import com.clova.anifriends.domain.payment.Payment;
import com.clova.anifriends.domain.payment.vo.PaymentStatus;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByOrderId(String orderId);

    @Modifying
    @Transactional
    @Query("update Payment p set p.status = :status where p.paymentId = :paymentId")
    void updatePaymentStatus(
        @Param("paymentId") Long paymentId,
        @Param("status") PaymentStatus status
    );

    @Modifying
    @Transactional
    @Query("update Payment p set p.paymentKey = :paymentKey, p.status = :status where p.paymentId = :paymentId")
    void updatePaymentKeyAndStatus(
        @Param("paymentId") Long paymentId,
        @Param("paymentKey") String paymentKey,
        @Param("status") PaymentStatus status
    );
}
