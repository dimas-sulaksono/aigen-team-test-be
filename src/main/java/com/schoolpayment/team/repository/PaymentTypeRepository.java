package com.schoolpayment.team.repository;

import com.schoolpayment.team.model.PaymentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentTypeRepository extends JpaRepository<PaymentType, Long> {

    Optional<PaymentType> findByPaymentTypeName(String name);

    Page<PaymentType> findByPaymentTypeNameContainingIgnoreCase(String name, Pageable pageable);
}
