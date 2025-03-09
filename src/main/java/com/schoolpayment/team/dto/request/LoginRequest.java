package com.schoolpayment.team.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "Email atau NIS tidak boleh kosong")
    private String username; // Bisa berupa email atau NIS

    @NotBlank(message = "Password tidak boleh kosong")
    private String password;
}
