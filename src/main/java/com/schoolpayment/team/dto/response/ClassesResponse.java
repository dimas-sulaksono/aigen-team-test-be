package com.schoolpayment.team.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ClassesResponse {
    private Long id;
    private String name;
    private String year;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
