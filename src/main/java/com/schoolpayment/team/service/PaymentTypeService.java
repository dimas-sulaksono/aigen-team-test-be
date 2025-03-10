package com.schoolpayment.team.service;

import com.schoolpayment.team.exception.DataNotFoundException;
import com.schoolpayment.team.exception.DuplicateDataException;
import com.schoolpayment.team.model.PaymentType;
import com.schoolpayment.team.repository.PaymentTypeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentTypeService {

    @Autowired
    PaymentTypeRepository paymentTypeRepository;

    @Transactional
    public void cratePaymentType(String name) {
        paymentTypeRepository.findByPaymentTypeName(name).ifPresent(paymentType -> { throw new DuplicateDataException("Payment type already exist"); });
        PaymentType newPaymentType = new PaymentType();
        newPaymentType.setPaymentTypeName(name);
        paymentTypeRepository.save(newPaymentType);
    }

    public Page<PaymentType> getAllPaymentType(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return paymentTypeRepository.findAll(pageable);
    }

    @Transactional
    public void updatePaymentType(Long id, String name) {
        PaymentType paymentType = paymentTypeRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Payment type not found"));
        if (paymentTypeRepository.findByPaymentTypeName(name).isPresent()) throw new DuplicateDataException("Payment type already exist");
        paymentType.setPaymentTypeName(name);
        paymentTypeRepository.save(paymentType);
    }

    @Transactional
    public void deletePaymentType(Long id) {
        PaymentType paymentType = paymentTypeRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Payment type not found"));
        paymentTypeRepository.delete(paymentType);
    }

    public Page<PaymentType> searchPaymentType(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return paymentTypeRepository.findByPaymentTypeNameContainingIgnoreCase(name, pageable);
    }
}
