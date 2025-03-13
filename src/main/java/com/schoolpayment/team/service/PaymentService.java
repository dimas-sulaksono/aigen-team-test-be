package com.schoolpayment.team.service;

import com.schoolpayment.team.dto.request.PaymentRequest;
import com.schoolpayment.team.dto.response.ExcelResponse;
import com.schoolpayment.team.dto.response.PaymentResponse;
import com.schoolpayment.team.exception.DataNotFoundException;
import com.schoolpayment.team.model.Payment;
import com.schoolpayment.team.model.PaymentType;
import com.schoolpayment.team.model.Student;
import com.schoolpayment.team.model.User;
import com.schoolpayment.team.repository.PaymentRepository;
import com.schoolpayment.team.repository.PaymentTypeRepository;
import com.schoolpayment.team.repository.StudentRepository;
import com.schoolpayment.team.util.ExcelGenerator;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private PaymentTypeRepository paymentTypeRepository;
    @Autowired
    private StudentRepository studentRepository;

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

    public Page<PaymentResponse> getPaymentByMe(User user, String name, String status, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Specification<Payment> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.equal(root.get("user"), user));
            if (name != null) {
                predicates.add(criteriaBuilder.like(root.get("paymentType").get("paymentTypeName"), "%" + name + "%"));
            }
            if (status != null) {
                predicates.add(criteriaBuilder.like(root.get("paymentStatus"), "%" + status + "%"));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Page<Payment> payments = paymentRepository.findAll(spec, pageable);
        return payments.map(this::convertToResponse);

    }

    public PaymentResponse createPayment(PaymentRequest request, User user) {

        PaymentType paymentType = paymentTypeRepository.findByPaymentTypeName(request.getPaymentType()).orElseThrow(() -> new DataNotFoundException("Payment type not found"));
        Student student = studentRepository.findByNis(user.getNis()).orElseThrow(() -> new DataNotFoundException("Student not found"));

        Payment payment = new Payment();
        payment.setPaymentName(request.getPaymentName());
        payment.setAmount(request.getAmount());
        payment.setPaymentStatus(request.getPaymentStatus());
        payment.setDescription(request.getDescription());
        payment.setUser(user);
        payment.setStudent(student);
        payment.setPaymentType(paymentType);

        Payment savedPayment = paymentRepository.save(payment);
        return convertToResponse(savedPayment);
    }

    public byte[] exportExcel(String status, String studentName, String userName, String schoolYear) throws IOException {
        Specification<Payment> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (status != null) predicates.add(criteriaBuilder.like(root.get("paymentStatus"), "%" + status + "%"));
            if (studentName != null) predicates.add(criteriaBuilder.like(root.get("student").get("name"), "%" + studentName + "%"));
            if (userName != null) predicates.add(criteriaBuilder.like(root.get("user").get("name"), "%" + userName + "%"));
            if (schoolYear != null) predicates.add(criteriaBuilder.like(root.get("student").get("classEntity").get("schoolYear").get("schoolYear"), "%" + schoolYear + "%"));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        List<ExcelResponse> paymentResponses = paymentRepository.findAll(spec)
                .stream()
                .map(this::convertToExcelResponse)
                .toList();

        return ExcelGenerator.generateExcel(paymentResponses);
    }



    private PaymentResponse convertToResponse(Payment payment) {
        PaymentResponse response = new PaymentResponse();
        response.setId(payment.getId());
        response.setName(payment.getPaymentName());
        response.setUser(payment.getUser().getId(), payment.getUser().getName());
        response.setStudent(payment.getStudent().getNis(), payment.getStudent().getName());
        response.setType(payment.getPaymentType().getPaymentTypeName());
        response.setSchoolYear((payment.getStudent().getClassEntity().getSchoolYear().getSchoolYear()));
        response.setAmount(payment.getAmount());
        response.setStatus(payment.getPaymentStatus());
        response.setDescription(payment.getDescription());
        response.setCreatedAt(payment.getCreatedAt());
        response.setUpdatedAt(payment.getUpdatedAt());
        response.setDeletedAt(payment.getDeletedAt());

        return response;
    }

    private ExcelResponse convertToExcelResponse(Payment payment) {
        ExcelResponse response = new ExcelResponse();
        response.setId(payment.getId());
        response.setName(payment.getPaymentName());
        response.setUserId(payment.getUser().getId());
        response.setUserName(payment.getUser().getName());
        response.setStudentNis(payment.getStudent().getNis());
        response.setStudentName(payment.getStudent().getName());
        response.setType(payment.getPaymentType().getPaymentTypeName());
        response.setAmount(payment.getAmount());
        response.setStatus(payment.getPaymentStatus());
        response.setDescription(payment.getDescription());
        response.setCreatedAt(payment.getCreatedAt());
        response.setUpdatedAt(payment.getUpdatedAt());
        response.setDeletedAt(payment.getDeletedAt());

        return response;
    }

}
