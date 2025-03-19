package com.schoolpayment.team.controller;

import com.schoolpayment.team.dto.request.StudentRequest;
import com.schoolpayment.team.dto.response.StudentResponse;
import com.schoolpayment.team.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    @Autowired
    private StudentService studentService;



    // ✅ Get all students with pagination
    @GetMapping
    public ResponseEntity<Page<StudentResponse>> getAllStudents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(studentService.getAllStudents(page, size));
    }

    // ✅ Create new student
    @PostMapping("/add")
    public ResponseEntity<StudentResponse> createStudent(@RequestBody StudentRequest studentRequest) {
        return ResponseEntity.ok(studentService.createStudent(studentRequest));
    }

    // ✅ Update student
    @PutMapping("/update/{id}")
    public ResponseEntity<StudentResponse> updateStudent(
            @PathVariable Long id,
            @RequestBody @Valid StudentRequest studentRequest) {
        return ResponseEntity.ok(studentService.updateStudent(id, studentRequest));
    }

    // ✅ Delete student
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok("Student with ID " + id + " deleted successfully");
    }

    // ✅ Search student by name (with pagination)
    @GetMapping("/search")
    public ResponseEntity<Page<StudentResponse>> searchByName(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(studentService.searchByName(name, page, size));
    }

    // ✅ Filter students by class ID (with pagination)
    @GetMapping("/filter")
    public ResponseEntity<Page<StudentResponse>> filterStudentsBySchoolYear(
            @RequestParam(defaultValue = "1970-01-01") String startDate,
            @RequestParam(defaultValue = "2200-12-31") String endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "asc") String sort) {

        Page<StudentResponse> students = studentService.filterStudentsBySchoolYear(
                startDate, endDate, page, size, sort);

        return ResponseEntity.ok(students);
    }


    // ✅ Sort students by name (with pagination)
    @GetMapping("/sort")
    public ResponseEntity<Page<StudentResponse>> sortStudentByName(
            @RequestParam String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(studentService.getStudentsSortedByName( sort,page, size));
    }

    @PatchMapping("/soft-delete/{id}")
    public ResponseEntity<String> softDeleteStudent(@PathVariable Long id) {
        try {
            studentService.softDeleteStudent(id);
            return ResponseEntity.ok("Student successfully soft deleted");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{username}")
    public ResponseEntity<StudentResponse> getStudentByUsername(@PathVariable String username) {
        return ResponseEntity.ok(studentService.findByusername(username));
    }
}
