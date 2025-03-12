package com.schoolpayment.team.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserRequest {

    @NotBlank(message = "NIS tidak boleh kosong")
    @Size(min = 5, max = 17, message = "NIS harus antara 5 hingga 17 karakter")
    private String nis;

    @Email(message = "Format email tidak valid")
    @NotBlank(message = "Email tidak boleh kosong")
    private String email;

    @NotBlank(message = "Nama tidak boleh kosong")
    @Size(min = 3, max = 255, message = "Nama harus antara 3 hingga 255 karakter")
    private String name;

    @NotBlank(message = "Password tidak boleh kosong")
    @Size(min = 6, message = "Password harus memiliki minimal 6 karakter")
    private String password;
    private MultipartFile images;
    private String role = "USER"; // Default role USER
}
