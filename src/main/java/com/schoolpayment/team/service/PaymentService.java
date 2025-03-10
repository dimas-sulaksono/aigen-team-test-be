package com.schoolpayment.team.service;

import com.schoolpayment.team.dto.response.PaymentResponse;
import com.schoolpayment.team.exception.DataNotFoundException;
import com.schoolpayment.team.model.ClassEntity;
import com.schoolpayment.team.model.Payment;
import com.schoolpayment.team.model.Student;
import com.schoolpayment.team.repository.PaymentRepository;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    public Page<PaymentResponse> getAllPayment(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Payment> payments = paymentRepository.findAll(pageable);
        return payments.map(this::convertToResponse);
    }

    public PaymentResponse getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Payment not found"));
        return convertToResponse(payment);
    }

    public Page<PaymentResponse> filterPayment(String status, String studentName, String userName, String schoolYear, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Specification<Payment> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (status != null) {
                predicates.add(criteriaBuilder.like(root.get("paymentStatus"), "%" + status + "%"));
            }

            if (studentName != null) {
                predicates.add(criteriaBuilder.like(root.get("student").get("name"), "%" + studentName + "%"));
            }

            if (userName != null) {
                predicates.add(criteriaBuilder.like(root.get("user").get("name"), "%" + userName + "%"));
            }
            if (schoolYear != null) {
                predicates.add(criteriaBuilder.like(root.get("student").get("classEntity").get("schoolYear").get("schoolYear"), "%" + schoolYear + "%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Page<Payment> payments = paymentRepository.findAll(spec, pageable);
        return payments.map(this::convertToResponse);

    }

    public Page<PaymentResponse> filterByName(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Payment> payments = paymentRepository.findByPaymentNameContainingIgnoreCase(name, pageable);
        return payments.map(this::convertToResponse);
    }

    public Page<PaymentResponse> searchPaymentByStudentName(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Payment> payments = paymentRepository.findByStudent_NameContainingIgnoreCase(name, pageable);
        return payments.map(this::convertToResponse);
    }

    public PaymentResponse updateStatusPayment(Long id, String status) {
        Payment payment = paymentRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Payment not found"));
        payment.setPaymentStatus(status);
        Payment updatedPayment = paymentRepository.save(payment);
        return convertToResponse(updatedPayment);
    }



    private PaymentResponse convertToResponse(Payment payment) {
        PaymentResponse response = new PaymentResponse();
        response.setId(payment.getId());
        response.setName(payment.getPaymentName());
        response.setUser(payment.getUser().getId(), payment.getUser().getName());
        response.setStudent(payment.getStudent().getNis(), payment.getStudent().getName());
        response.setType(payment.getPaymentType());
        response.setAmount(payment.getAmount());
        response.setStatus(payment.getPaymentStatus());
        response.setDescription(payment.getDescription());
        response.setCreatedAt(payment.getCreatedAt());
        response.setUpdatedAt(payment.getUpdatedAt());
        response.setDeletedAt(payment.getDeletedAt());

        return response;
    }


}
