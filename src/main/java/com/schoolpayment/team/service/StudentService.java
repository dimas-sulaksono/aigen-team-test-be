package com.schoolpayment.team.service;

import com.schoolpayment.team.dto.request.StudentRequest;
import com.schoolpayment.team.dto.response.StudentResponse;
import com.schoolpayment.team.exception.DataNotFoundException;
import com.schoolpayment.team.model.ClassEntity;
import com.schoolpayment.team.model.Student;
import com.schoolpayment.team.repository.ClassesRepository;
import com.schoolpayment.team.repository.StudentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ClassesRepository classesRepository;




    @Transactional
    public Page<StudentResponse> getAllStudents(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Student> studentPage = studentRepository.findAll(pageable);
        return studentPage.map(this::convertToStudentResponse);
    }

    @Transactional
    public StudentResponse createStudent(StudentRequest studentRequest) {
        try {
            ClassEntity classEntity = classesRepository.findById(studentRequest.getClassId()).orElseThrow(() -> new DataNotFoundException("Class not found"));
            Student student = new Student();
            student.setNis(studentRequest.getNis());
            student.setName(studentRequest.getName());
            student.setClassEntity(classEntity);
            student.setAddress(studentRequest.getAddress());
            student.setPhoneNumber(studentRequest.getPhoneNumber());
            Student savedStudent = studentRepository.save(student);
            return convertToStudentResponse(savedStudent);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create student", e);
        }
    }

    @Transactional
    public StudentResponse updateStudent(Long id, StudentRequest studentRequest) {
        try {
            // Cari siswa berdasarkan id
            Student student = studentRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            // Update hanya jika nilai ada di request
            if (studentRequest.getNis() != null) {
                student.setNis(studentRequest.getNis());
            }
            if (studentRequest.getName() != null) {
                student.setName(studentRequest.getName());
            }
            if (studentRequest.getAddress() != null) {
                student.setAddress(studentRequest.getAddress());
            }
            if (studentRequest.getPhoneNumber() != null) {
                student.setPhoneNumber(studentRequest.getPhoneNumber());
            }

            // Simpan perubahan
            Student updatedStudent = studentRepository.save(student);

            // Kembalikan response yang sudah diperbarui
            return convertToStudentResponse(updatedStudent);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update student", e);
        }
    }


    @Transactional
    public void deleteStudent(Long id) {
        try {
            studentRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete student", e);
        }
    }

    @Transactional
    public Page<StudentResponse> searchByName(String name, int page, int size) {
      try{
          Pageable pageable = PageRequest.of( page, size);
          Page<Student> studentPage = studentRepository.findByName(name, pageable);
          return studentPage.map(this::convertToStudentResponse);
      } catch (Exception e) {
          throw new RuntimeException("Failed to search student", e);
      }
    }

    @Transactional
    public Page<StudentResponse> filterStudentsBySchoolYear(String startDate, String endDate, int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Student> studentPage = studentRepository.findAllByClassEntity_SchoolYear_SchoolYearBetween(startDate, endDate, pageable);
            return studentPage.map(this::convertToStudentResponse);
        } catch (Exception e) {
            throw new RuntimeException("Failed to filter students by school year", e);
        }
    }

    @Transactional
    public Page<StudentResponse> getStudentsSortedByName(String sort, int page, int size) {
        try{
            Pageable pageable = PageRequest.of(page, size);
            Page<Student> studentPage;
            if(sort.equals("asc")) {
                studentPage = studentRepository.findAllByOrderByNameAsc(pageable);
            }else {
                studentPage = studentRepository.findAllByOrderByNameDesc(pageable);
            }
            return studentPage.map(this::convertToStudentResponse);
        }catch (Exception e) {
            throw new RuntimeException("Failed to get students sorted by name", e);
        }
    }

    @Transactional
    public void softDeleteStudent(Long id) {
        try {
            if(!studentRepository.existsById(id)){
                throw new RuntimeException("Student not found");
            }
            Student student = studentRepository.findById(id).orElseThrow(()-> new DataNotFoundException("Student not found"));
            student.setDeletedAt(LocalDateTime.now());
            studentRepository.save(student);

        } catch (Exception e) {
            throw new RuntimeException("Failed to soft delete student", e);
        }
    }
    public StudentResponse convertToStudentResponse(Student student) {
      StudentResponse response = new StudentResponse(student);
      response.setNis(student.getNis());
      response.setName(student.getName());
      response.setClassName(student.getClassEntity().getClassName());
      response.setBirthdate(student.getBirthdate());
      response.setAddress(student.getAddress());
      response.setPhoneNumber(student.getPhoneNumber());
      response.setCreatedAt(student.getCreatedAt());
      response.setUpdatedAt(student.getUpdatedAt());
      response.setDeletedAt(student.getDeletedAt());
      return response;
    }

}
