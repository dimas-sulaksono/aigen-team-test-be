package com.schoolpayment.team.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequest {

    @NotBlank(message = "Nama pembayaran tidak boleh kosong")
    private String paymentName;

    @NotNull(message = "Jumlah pembayaran tidak boleh kosong")
    private BigDecimal amount;

    @NotBlank(message = "Status pembayaran tidak boleh kosong")
    private String paymentStatus;

    private String description;

    @NotBlank(message = "Jenis pembayaran tidak boleh kosong")
    private String paymentType;

}
