package com.schoolpayment.team.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SchoolYearRequest {
    private String schoolYear;
    private LocalDate startDate;
    private LocalDate endDate;
}
