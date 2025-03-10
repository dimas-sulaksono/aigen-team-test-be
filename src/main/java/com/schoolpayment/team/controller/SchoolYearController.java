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


    @GetMapping("/all")
    public ResponseEntity<?> getSchoolYears(
            @RequestParam (defaultValue = "0") int page,
            @RequestParam (defaultValue = "10") int size) {

        Page<SchoolYearResponse> response = schoolYearService.getSchoolYears(page, size);
        return ResponseEntity.ok(new ApiResponse<>(200, response));
    }

    @PostMapping("/add")
    public ResponseEntity<?> createSchoolYear(@RequestBody @Valid SchoolYearRequest schoolYear) {
        SchoolYearResponse response = schoolYearService.createSchoolYear(schoolYear);
        return ResponseEntity.ok(new ApiResponse<>(200, response));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateSchoolYear(@PathVariable Long id, @RequestBody @Valid SchoolYearRequest schoolYear) {
        SchoolYearResponse response = schoolYearService.updateSchoolYear(id, schoolYear);
        return ResponseEntity.ok(new ApiResponse<>(200, response));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteSchoolYear(@PathVariable Long id) {
        schoolYearService.deleteSchoolYear(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "School year deleted successfully"));
    }

    @PutMapping("/soft-delete/{id}")
    public ResponseEntity<?> softDeleteSchoolYear(@PathVariable Long id) {
        schoolYearService.softDeleteSchoolYear(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "School year soft deleted successfully"));
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchSchoolYear(
            @RequestParam String years,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Page<SchoolYearResponse> response = schoolYearService.getSearch(years, page, size);
        return ResponseEntity.ok(new ApiResponse<>(200, response));
    }
}
