package com.schoolpayment.team.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateClassesRequest {

    @NotBlank(message = "Nama kelas tidak boleh kosong")
    private String name;

    private Long schoolYearId;
}
