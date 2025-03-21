package com.schoolpayment.team.service;

import com.schoolpayment.team.dto.request.ClassesRequest;
import com.schoolpayment.team.dto.request.UpdateClassesRequest;
import com.schoolpayment.team.dto.response.ClassesResponse;
import com.schoolpayment.team.exception.DataNotFoundException;
import com.schoolpayment.team.exception.DuplicateDataException;
import com.schoolpayment.team.model.ClassEntity;
import com.schoolpayment.team.model.SchoolYear;
import com.schoolpayment.team.repository.ClassesRepository;
import com.schoolpayment.team.repository.SchoolYearRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ClassesService {

    @Autowired
    private ClassesRepository classesRepository;
    @Autowired
    private SchoolYearRepository schoolYearRepository;

    public ClassesResponse addNewClass(ClassesRequest request) {
        SchoolYear schoolYear = schoolYearRepository.findById(request.getSchoolYearId()).orElseThrow(() -> new DataNotFoundException("School year not found"));
        if (classesRepository.existsByClassNameIgnoreCaseAndSchoolYear(request.getName(), schoolYear)) throw new DuplicateDataException("Class with this name and school year already exists");

        ClassEntity classesEntity = new ClassEntity();
        classesEntity.setClassName(request.getName());
        classesEntity.setSchoolYear(schoolYear);
        return mapToClassesResponse(classesRepository.save(classesEntity));

    }

    public ClassesResponse updateClass(Long id, UpdateClassesRequest request) {
        ClassEntity classesEntity = classesRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Class not found"));
        if (request.getSchoolYearId() != null) {
            SchoolYear schoolYear = schoolYearRepository.findById(request.getSchoolYearId()).orElseThrow(() -> new DataNotFoundException("School year not found"));
            if (classesRepository.existsByClassNameIgnoreCaseAndSchoolYear(request.getName(), schoolYear)) throw new DuplicateDataException("Class with this name and school year already exists");
            classesEntity.setSchoolYear(schoolYear);
        } else {
            if (classesRepository.existsByClassNameIgnoreCaseAndSchoolYear(request.getName(), classesEntity.getSchoolYear())) throw new DuplicateDataException("Class with this name and school year already exists");
        }
        classesEntity.setClassName(request.getName());
        return mapToClassesResponse(classesRepository.save(classesEntity));
    }

    public void deleteClass(Long id) {
        ClassEntity classesEntity = classesRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Class not found"));
        classesRepository.delete(classesEntity);
    }

    public void softDeleteClass(Long id) {
        ClassEntity classesEntity = classesRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Class not found"));
        classesEntity.setDeletedAt(java.time.LocalDateTime.now());
        classesRepository.save(classesEntity);
    }

    public Page<ClassesResponse> getAllClasses(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "schoolYear.schoolYear"));
        return classesRepository.findAllByOrderByClassNameAsc(pageable).map(this::mapToClassesResponse);
    }

    public Page<ClassesResponse> searchClasses(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "schoolYear.schoolYear"));
        return classesRepository.findAllByClassNameContainingIgnoreCaseOrderByClassNameAsc(name,pageable).map(this::mapToClassesResponse);
    }

    private ClassesResponse mapToClassesResponse(ClassEntity classesEntity) {
        ClassesResponse classesResponse = new ClassesResponse();
        classesResponse.setId(classesEntity.getId());
        classesResponse.setName(classesEntity.getClassName());
        classesResponse.setYear(classesEntity.getSchoolYear().getSchoolYear());
        classesResponse.setYearId(classesEntity.getSchoolYear().getId());
        classesResponse.setCreatedAt(classesEntity.getCreatedAt());
        classesResponse.setUpdatedAt(classesEntity.getUpdatedAt());
        classesResponse.setDeletedAt(classesEntity.getDeletedAt());
        return classesResponse;
    }


}
