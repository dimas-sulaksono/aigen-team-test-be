package com.schoolpayment.team.repository;

import com.schoolpayment.team.model.ClassEntity;
import com.schoolpayment.team.model.SchoolYear;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClassesRepository extends JpaRepository<ClassEntity, Long>, JpaSpecificationExecutor<ClassEntity> {
    Boolean existsByClassNameIgnoreCaseAndSchoolYear(@NotBlank String name, SchoolYear schoolYear);

    Page<ClassEntity> findAllByOrderByClassNameAsc(Pageable pageable);

    Page<ClassEntity> findAllByClassNameContainingIgnoreCaseOrderByClassNameAsc(String name, Pageable pageable);
}
