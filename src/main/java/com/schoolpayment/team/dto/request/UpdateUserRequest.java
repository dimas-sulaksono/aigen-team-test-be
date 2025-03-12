package com.schoolpayment.team.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdateUserRequest {
    private String nis;
    private String email;
    private String name;
    private String password;
    private MultipartFile image;
    private String role;
}
