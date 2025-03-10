package com.schoolpayment.team.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StudentResponse {
    private Long id;
    private String nis;
    private String name;
    private String address;
    private String phoneNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
