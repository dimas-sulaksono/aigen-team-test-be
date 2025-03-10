package com.schoolpayment.team.repository;

import com.schoolpayment.team.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {

}