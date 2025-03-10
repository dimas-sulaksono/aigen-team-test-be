package com.schoolpayment.team.repository;

import com.schoolpayment.team.model.SchoolYear;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolYearRepository extends JpaRepository<SchoolYear,Long> {
    SchoolYear findBySchoolYear(String schoolYear);


    Page<SchoolYear> findBySchoolYearContainingAndDeletedAtIsNull(String schoolYear, Pageable pageable);
}
