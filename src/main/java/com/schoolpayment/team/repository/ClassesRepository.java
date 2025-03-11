package com.schoolpayment.team.repository;

import com.schoolpayment.team.model.ClassEntity;
import com.schoolpayment.team.model.SchoolYear;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassesRepository extends JpaRepository<ClassEntity, Long>, JpaSpecificationExecutor<ClassEntity> {
    Boolean existsByClassNameAndSchoolYear(@NotBlank String name, SchoolYear schoolYear);
}
