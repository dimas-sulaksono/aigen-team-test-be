package com.schoolpayment.team.dto.response;

import com.schoolpayment.team.model.PaymentType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ExcelResponse {
    private Long id;
    private String name;
    private Long userId;
    private String userName;
    private String studentNis;
    private String studentName;
    private String type;
    private BigDecimal amount;
    private String status;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
