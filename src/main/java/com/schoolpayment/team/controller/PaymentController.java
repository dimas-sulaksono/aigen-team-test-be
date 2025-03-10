package com.schoolpayment.team.controller;

import com.schoolpayment.team.dto.response.ApiResponse;
import com.schoolpayment.team.exception.UserNotFoundException;
import com.schoolpayment.team.model.PaymentType;
import com.schoolpayment.team.security.CustomUserDetails;
import com.schoolpayment.team.service.PaymentTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment-type")
public class PaymentController {

    @Autowired
    private PaymentTypeService paymentTypeService;

    // add payment type
    @PostMapping("/add")
    public ResponseEntity<?> addPaymentType(@RequestParam String name) {

        paymentTypeService.cratePaymentType(name);
        return ResponseEntity.ok(new ApiResponse<>(200, "success add payment type"));
    }

    // update payment type
    @PutMapping("/update")
    public ResponseEntity<?> updatePaymentType(@RequestParam Long id, @RequestParam String name) {
        paymentTypeService.updatePaymentType(id, name);
        return ResponseEntity.ok(new ApiResponse<>(200, "success update payment type"));
    }

    // delete payment type
    @DeleteMapping("/delete")
    public ResponseEntity<?> deletePaymentType(@RequestParam Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        paymentTypeService.deletePaymentType(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "success delete payment type"));
    }

    // get all payment type
    @GetMapping
    public ResponseEntity<?> getAllPaymentType(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "5") int size) {
        Page<PaymentType> payments = paymentTypeService.getAllPaymentType(page, size);
        return ResponseEntity.ok(new ApiResponse<>(200, payments));

    }

    @GetMapping("/search")
    public ResponseEntity<?> searchPaymentType(@RequestParam String name, @RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "5") int size) {
        Page<PaymentType> payments = paymentTypeService.searchPaymentType(name, page, size);
        return ResponseEntity.ok(new ApiResponse<>(200, payments));
    }

}
