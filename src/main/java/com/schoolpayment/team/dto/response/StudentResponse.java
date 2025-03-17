package com.schoolpayment.team.dto.response;

import com.schoolpayment.team.model.Student;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class StudentResponse {
    private Long id;
    private String nis;
    private String name;
    private String className;
    private long classId;
    private LocalDate birthdate;
    private LocalDate startDate;
    private LocalDate endDate;
    private String address;
    private String phoneNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public StudentResponse(Student student) {
        this.id = student.getId();
        this.nis = student.getNis();
        this.name = student.getName();
        this.className = student.getClassEntity().getClassName();
        this.classId = student.getClassEntity().getId();
        this.birthdate = student.getBirthdate();
        this.startDate = student.getStartDate();
        this.endDate = student.getEndDate();
        this.address = student.getAddress();
        this.phoneNumber = student.getPhoneNumber();
        this.createdAt = student.getCreatedAt();
        this.updatedAt = student.getUpdatedAt();
        this.deletedAt = student.getDeletedAt();
    }

}
