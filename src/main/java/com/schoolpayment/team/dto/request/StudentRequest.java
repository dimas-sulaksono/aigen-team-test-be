package com.schoolpayment.team.dto.request;


import lombok.Data;

import java.time.LocalDate;

@Data
public class StudentRequest {
    private String nis;
    private String name;
    private int classId;
    private String address;
    private String phoneNumber;

    public LocalDate getBirthdate() {
        return LocalDate.now();
    }
}
