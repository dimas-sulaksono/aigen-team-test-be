package com.schoolpayment.team.repository;

import com.schoolpayment.team.model.Student;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Page<Student> findByName(String name, Pageable pageable);

    Page<Student> findByClassId(Long classId, Pageable pageable);


    Page<Student> findAllByOrderByNameAsc(Pageable pageable);

    Page<Student> findAllByOrderByNameDesc(Pageable pageable);

    Page<Student> findAllByClassEntity_SchoolYear_SchoolYearBetween(String startDate, String endDate, Pageable pageable);
    Optional<Student> findByNis(@NotBlank(message = "NIS tidak boleh kosong") String nis);
}