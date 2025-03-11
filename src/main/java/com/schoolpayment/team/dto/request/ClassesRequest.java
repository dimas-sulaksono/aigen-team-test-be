package com.schoolpayment.team.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ClassesRequest {

    @NotBlank(message = "Nama kelas tidak boleh kosong")
    private String name;
    @NotNull(message = "Tahun ajaran tidak boleh kosong")
    private Long schoolYearId;
}
