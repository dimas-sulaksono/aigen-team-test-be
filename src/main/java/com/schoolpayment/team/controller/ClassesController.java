package com.schoolpayment.team.controller;

import com.schoolpayment.team.dto.request.ClassesRequest;
import com.schoolpayment.team.dto.request.UpdateClassesRequest;
import com.schoolpayment.team.dto.response.ApiResponse;
import com.schoolpayment.team.dto.response.ClassesResponse;
import com.schoolpayment.team.service.ClassesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/class")
public class ClassesController {

    @Autowired
    private ClassesService classesService;

    @PostMapping("/add")
    public ResponseEntity<?> addNewClass(@RequestBody ClassesRequest request) {
        ClassesResponse response = classesService.addNewClass(request);
        return ResponseEntity.ok().body(new ApiResponse<>(200, response));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateClass(
            @RequestBody UpdateClassesRequest request,
            @PathVariable Long id) {
        ClassesResponse response = classesService.updateClass(id, request);
        return ResponseEntity.ok().body(new ApiResponse<>(200, response));
    }

    @PatchMapping("/soft-delete/{id}")
    public ResponseEntity<?> softDeleteClass(@PathVariable Long id) {
        classesService.softDeleteClass(id);
        return ResponseEntity.ok().body(new ApiResponse<>(200, "Class deleted successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteClass(@PathVariable Long id) {
        classesService.deleteClass(id);
        return ResponseEntity.ok().body(new ApiResponse<>(200, "Class deleted successfully"));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllClasses(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok().body(classesService.getAllClasses(page, size));
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchClasses(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return ResponseEntity.ok().body(classesService.searchClasses(name, page, size));
    }


}
