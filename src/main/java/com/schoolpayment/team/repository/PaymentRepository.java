package com.schoolpayment.team.repository;

import com.schoolpayment.team.model.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>, JpaSpecificationExecutor<Payment> {

    Page<Payment> findByPaymentNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Payment> findByStudent_NameContainingIgnoreCase(String name, Pageable pageable);

    @Query("SELECT SUM(p.amount) FROM Payment p WHERE LOWER(p.paymentStatus) = 'paid'")
    BigDecimal sumPaidAmount();

    @Query("SELECT SUM(p.amount) FROM Payment p WHERE LOWER(p.paymentStatus) = 'pending'")
    BigDecimal sumPendingAmount();
}
