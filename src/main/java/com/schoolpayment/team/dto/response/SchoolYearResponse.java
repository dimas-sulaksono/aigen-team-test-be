package com.schoolpayment.team.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class SchoolYearResponse {
    private Long id;
    private String schoolYear;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime deletedAt;
}
