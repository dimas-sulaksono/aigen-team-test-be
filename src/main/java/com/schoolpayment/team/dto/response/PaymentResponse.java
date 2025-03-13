package com.schoolpayment.team.dto.response;

import com.schoolpayment.team.model.PaymentType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentResponse {
    private Long id;
    private String name;
    private UserPayment user = new UserPayment();
    private StudentPayment student = new StudentPayment();
    private String type;
    private String schoolYear;
    private BigDecimal amount;
    private String status;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public void setUser(Long id, String name) {
        user.setId(id);
        user.setName(name);
    }

    public void setStudent(String nis, String name) {
        student.setNis(nis);
        student.setName(name);
    }
}

@Data
class UserPayment {
    private Long id;
    private String name;
}

@Data
class StudentPayment {
    private String nis;
    private String name;
}
