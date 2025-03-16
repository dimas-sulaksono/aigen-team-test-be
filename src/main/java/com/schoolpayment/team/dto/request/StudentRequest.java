package com.schoolpayment.team.dto.request;


import com.schoolpayment.team.model.ClassEntity;
import lombok.Data;

import java.time.LocalDate;

@Data
public class StudentRequest {
    private String nis;
    private String name;
    private Long classId;
    private String address;
    private String phoneNumber;

    public LocalDate getBirthdate() {
        return LocalDate.now();
    }

}
