package com.schoolpayment.team.controller;

import com.schoolpayment.team.dto.response.PaymentResponse;
import com.schoolpayment.team.model.Payment;
import com.schoolpayment.team.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/all")
    public Page<PaymentResponse> getAllPayment(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        return paymentService.getAllPayment(page, size);
    }

    @GetMapping("/{id}")
    public PaymentResponse getPaymentById(@PathVariable Long id) {
        return paymentService.getPaymentById(id);
    }

    @GetMapping("/filter")
    public Page<PaymentResponse> getPaymentByFilter(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String student,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String year,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return paymentService.filterPayment(status, student, username, year, page, size);
    }

    @GetMapping("/name/{name}")
    public Page<PaymentResponse> getPaymentByName(@PathVariable String name, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        return paymentService.filterByName(name, page, size);
    }

    @GetMapping("/search")
    public Page<PaymentResponse> getPaymentByStudentName(@RequestParam String student, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        return paymentService.searchPaymentByStudentName(student, page, size);
    }

    @PutMapping("/{id}")
    public PaymentResponse updateStatusPayment(@PathVariable Long id, @RequestParam String status) {
        return paymentService.updateStatusPayment(id, status);
    }

}
