package com.schoolpayment.team.service;

import com.schoolpayment.team.dto.request.SchoolYearRequest;
import com.schoolpayment.team.dto.response.SchoolYearResponse;
import com.schoolpayment.team.exception.DataNotFoundException;
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
        try {
            SchoolYear existingSchoolYear = schoolYearRepository.findBySchoolYear(request.getSchoolYear());
            if (existingSchoolYear != null) {
                throw new DuplicateDataException("School year already exists: " + request.getSchoolYear());
            }

            SchoolYear newSchoolYear = new SchoolYear();
            newSchoolYear.setSchoolYear(request.getSchoolYear());
            newSchoolYear.setStartDate(request.getStartDate());
            newSchoolYear.setEndDate(request.getEndDate());
            newSchoolYear = schoolYearRepository.save(newSchoolYear);

            return convertToResponse(newSchoolYear);
        } catch (DuplicateDataException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create school year", e);
        }
    }

    @Transactional
    public SchoolYearResponse updateSchoolYear(Long id, SchoolYearRequest request) {
        try{
            SchoolYear existingSchoolYear = schoolYearRepository.findById(id)
                    .orElse(null);
            if (existingSchoolYear == null) {
                throw new DataNotFoundException("School year not found: " + id);
            }
            existingSchoolYear.setSchoolYear(request.getSchoolYear());
            existingSchoolYear.setStartDate(request.getStartDate());
            existingSchoolYear.setEndDate(request.getEndDate());
            existingSchoolYear = schoolYearRepository.save(existingSchoolYear);
            return convertToResponse(existingSchoolYear);
        }catch (DataNotFoundException e){
            throw new RuntimeException(e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update school year");
        }
    }

    public void deleteSchoolYear(Long id) {
        try{
            SchoolYear existingSchoolYear = schoolYearRepository.findById(id)
                    .orElseThrow(() -> new DataNotFoundException("School year not found: " + id));
            schoolYearRepository.delete(existingSchoolYear);
        }catch (DataNotFoundException e){
            throw new RuntimeException(e.getMessage(), e);
        }catch (Exception e) {
            throw new RuntimeException("Failed to delete school year", e);
        }
    }

    public void softDeleteSchoolYear(Long id) {
        try {
            SchoolYear existingSchoolYear = schoolYearRepository.findById(id)
                    .orElseThrow(() -> new DataNotFoundException("School year not found: " + id));
            existingSchoolYear.setDeletedAt(java.time.LocalDateTime.now());
            existingSchoolYear = schoolYearRepository.save(existingSchoolYear);
            convertToResponse(existingSchoolYear);
        } catch (DataNotFoundException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to soft delete school year", e);
        }
    }


    public Page<SchoolYearResponse> getSearch(String schoolYear, int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<SchoolYear> schoolYears = schoolYearRepository.findBySchoolYearContainingAndDeletedAtIsNull(schoolYear, pageable);
            if (!schoolYears.isEmpty()) {
                return schoolYears.map(this::convertToResponse);
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve active school years");
        }
    }

    private SchoolYearResponse convertToResponse(SchoolYear schoolYear) {
        SchoolYearResponse response = new SchoolYearResponse();
        response.setId(schoolYear.getId());
        response.setSchoolYear(schoolYear.getSchoolYear());
        response.setStartDate(schoolYear.getStartDate());
        response.setEndDate(schoolYear.getEndDate());
        response.setDeletedAt(schoolYear.getDeletedAt());
        return response;
    }
}
