package com.schoolpayment.team.repository;

import com.schoolpayment.team.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Page<Student> findByName(String name, Pageable pageable);

    Page<Student> findByClassId(Long classId, Pageable pageable);



    Page<Student> findAllByOrderByNameAsc(Pageable pageable);

    Page<Student> findAllByOrderByNameDesc(Pageable pageable);

    @Query("SELECT s FROM Student s WHERE s.createdAt >= :startDate AND s.createdAt <= :endDate")
    Page<Student> findBySchoolYear(LocalDate startDate, LocalDate endDate, Pageable pageable);

    void softDeleteById(Long id);
}