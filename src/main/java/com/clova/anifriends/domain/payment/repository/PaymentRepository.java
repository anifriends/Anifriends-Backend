package com.clova.anifriends.domain.payment.repository;

import com.clova.anifriends.domain.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
