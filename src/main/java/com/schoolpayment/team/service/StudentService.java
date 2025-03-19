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
            boolean exists = studentRepository.existsByNis(studentRequest.getNis());
            if (exists) {
                throw new RuntimeException("Student with this NIS already exists");
            }
            ClassEntity classEntity = classesRepository.findById(studentRequest.getClassId())
                    .orElseThrow(() -> new DataNotFoundException("Class not found"));
            Student student = new Student();
            student.setNis(studentRequest.getNis());
            student.setName(studentRequest.getName());
            student.setClassEntity(classEntity);
            student.setAddress(studentRequest.getAddress());
            student.setPhoneNumber(studentRequest.getPhoneNumber());
            student.setBirthdate(studentRequest.getBirthdate());

            Student savedStudent = studentRepository.save(student);

            return convertToStudentResponse(savedStudent);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create student", e);
        }
    }


    @Transactional
    public StudentResponse updateStudent(Long id, StudentRequest studentRequest) {
        try {
            Student student = studentRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            if ((studentRequest.getNis() == null || studentRequest.getNis().equals(student.getNis())) &&
                    (studentRequest.getName() == null || studentRequest.getName().equals(student.getName())) &&
                    (studentRequest.getAddress() == null || studentRequest.getAddress().equals(student.getAddress())) &&
                    (studentRequest.getPhoneNumber() == null || studentRequest.getPhoneNumber().equals(student.getPhoneNumber()))&&
                    (studentRequest.getBirthdate() == null || studentRequest.getBirthdate().equals(student.getBirthdate()))&&
                    (studentRequest.getClassId() == null || studentRequest.getClassId().equals(student.getClassEntity().getId())))
            {
                throw new RuntimeException("No changes detected, update not performed");
            }

            if (studentRequest.getNis() != null && !studentRequest.getNis().equals(student.getNis())) {
                throw new RuntimeException("NIS cannot be changed");
            }
            if (studentRequest.getName() != null && !studentRequest.getName().equals(student.getName())) {
                student.setName(studentRequest.getName());
            }
            if (studentRequest.getAddress() != null && !studentRequest.getAddress().equals(student.getAddress())) {
                student.setAddress(studentRequest.getAddress());
            }
            if (studentRequest.getPhoneNumber() != null && !studentRequest.getPhoneNumber().equals(student.getPhoneNumber())) {
                student.setPhoneNumber(studentRequest.getPhoneNumber());
            }
            if (studentRequest.getBirthdate() != null && !studentRequest.getBirthdate().equals(student.getBirthdate())) {
                student.setBirthdate(studentRequest.getBirthdate());
            }
            if (studentRequest.getClassId() != null && !studentRequest.getClassId().equals(student.getClassEntity().getId())) {
                ClassEntity classEntity = classesRepository.findById(studentRequest.getClassId())
                        .orElseThrow(() -> new DataNotFoundException("Class not found"));
                student.setClassEntity(classEntity);
            }
            

            Student updatedStudent = studentRepository.save(student);

            return convertToStudentResponse(updatedStudent);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update student", e);
        }
    }



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

    @Transactional
    public StudentResponse findByusername(String username) {
        try {
            Student student = studentRepository.findByname(username).orElseThrow(() -> new DataNotFoundException("Student not found"));
            return convertToStudentResponse(student);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get student by username", e);
        }
    }
    public StudentResponse convertToStudentResponse(Student student) {
      StudentResponse response = new StudentResponse(student);
      return response;
    }

}
