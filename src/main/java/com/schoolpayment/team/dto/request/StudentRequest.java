package com.schoolpayment.team.dto.request;


import lombok.Data;

@Data
public class StudentRequest {
    private String nis;
    private String name;
    private int classId;
    private String address;
    private String phoneNumber;
}
