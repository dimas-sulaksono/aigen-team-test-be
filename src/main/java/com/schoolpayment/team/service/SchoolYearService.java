package com.schoolpayment.team.service;

import com.schoolpayment.team.dto.request.SchoolYearRequest;
import com.schoolpayment.team.dto.response.SchoolYearResponse;
import com.schoolpayment.team.exception.DuplicateDataException;
import com.schoolpayment.team.model.SchoolYear;
import com.schoolpayment.team.repository.SchoolYearRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SchoolYearService {
    @Autowired
    private SchoolYearRepository schoolYearRepository;

    @Transactional
    public Page<SchoolYearResponse> getSchoolYears(int page, int size) {
         try {
             Pageable pageable = PageRequest.of(page, size);
             Page<SchoolYear> schoolYears = schoolYearRepository.findAll(pageable);
             if (!schoolYears.isEmpty()) {
                return schoolYears.map(this::convertToResponse);
             } else {
                return null;
             }
         }catch (Exception e) {
             throw new RuntimeException("Failed to retrieve school years");
         }
    }

    @Transactional
    public SchoolYearResponse createSchoolYear(SchoolYearRequest request) {
      try{
          SchoolYear savedSchoolYear = new SchoolYear();
          SchoolYear existingSchoolYear = schoolYearRepository.findBySchoolYear(request.getSchoolYear());
          if (existingSchoolYear != null) {
              throw new DuplicateDataException("School year already exists: " + request.getSchoolYear());
          }
          savedSchoolYear.setSchoolYear(request.getSchoolYear());
          savedSchoolYear.setStartDate(request.getStartDate());
          savedSchoolYear.setEndDate(request.getEndDate());
          savedSchoolYear = schoolYearRepository.save(savedSchoolYear);
          return convertToResponse(savedSchoolYear);
      } catch (Exception e) {
          throw new RuntimeException("Failed to create school year");
      }
    }

    private SchoolYearResponse convertToResponse(SchoolYear schoolYear) {
        SchoolYearResponse response = new SchoolYearResponse();
        response.setId(schoolYear.getId());
        response.setSchoolYear(schoolYear.getSchoolYear());
        response.setStartDate(schoolYear.getStartDate());
        response.setEndDate(schoolYear.getEndDate());
        return response;
    }
}
