package com.schoolpayment.team.controller;

import com.schoolpayment.team.dto.request.SchoolYearRequest;
import com.schoolpayment.team.dto.response.ApiResponse;
import com.schoolpayment.team.dto.response.SchoolYearResponse;
import com.schoolpayment.team.service.SchoolYearService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/school-years")
public class SchoolYearController {
    @Autowired
    private SchoolYearService schoolYearService;


    @GetMapping
    public ResponseEntity<?> getSchoolYears(
            @RequestParam (defaultValue = "0") int page,
            @RequestParam (defaultValue = "10") int size) {

        Page<SchoolYearResponse> response = schoolYearService.getSchoolYears(page, size);
        return ResponseEntity.ok(new ApiResponse<>(200, response));
    }

    @PostMapping
    public ResponseEntity<?> createSchoolYear(@RequestBody @Valid SchoolYearRequest schoolYear) {
        SchoolYearResponse response = schoolYearService.createSchoolYear(schoolYear);
        return ResponseEntity.ok(new ApiResponse<>(200, response));
    }
}
