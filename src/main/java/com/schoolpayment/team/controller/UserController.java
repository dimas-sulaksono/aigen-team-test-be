package com.schoolpayment.team.controller;

import com.schoolpayment.team.dto.request.LoginRequest;
import com.schoolpayment.team.dto.request.UpdateUserRequest;
import com.schoolpayment.team.dto.request.UserRequest;
import com.schoolpayment.team.dto.response.ApiResponse;
import com.schoolpayment.team.dto.response.UserResponse;
import com.schoolpayment.team.exception.DataNotFoundException;
import com.schoolpayment.team.exception.DuplicateDataException;
import com.schoolpayment.team.service.UserService;
import com.schoolpayment.team.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/user")
public class UserController {

    @Autowired
    private UserService userService;


    @Autowired
    private JwtUtil jwtUtil;


    // register
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRequest userRequest){
        try {
            UserResponse userResponse = userService.registerUser(userRequest);
            return ResponseEntity.ok(new ApiResponse<>(200, userResponse));
        } catch (DuplicateDataException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse<>(409, e.getMessage()));
        } catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(400, e.getMessage()));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, e.getMessage()));
        }
    }

    // login
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        UserResponse userResponse = userService.loginUser(loginRequest);
        String token = jwtUtil.generateToken(userResponse.getEmail(), userResponse.getRole());
        return ResponseEntity.ok(new ApiResponse<>(200, token));
    }

    // get all
    @GetMapping("/all")
    public ResponseEntity<?> findAll(int page, int size){
        try {
            Page<UserResponse> users = userService.findAll(page, size);
            return ResponseEntity.ok(new ApiResponse<>(200, users));
        } catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(400, e.getMessage()));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, e.getMessage()));
        }
    }

    // filter
    @GetMapping("/filter")
    public ResponseEntity<?> filterUsersByRole(@RequestParam String role, int page, int size){
        try {
            Page<UserResponse> users = userService.filterUsersByRole(role, page, size);
            return ResponseEntity.ok(new ApiResponse<>(200, users));
        } catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(400, e.getMessage()));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, e.getMessage()));
        }
    }

    // update
    @PutMapping(value = "/update/{id}", consumes = "multipart/form-data")
    public ResponseEntity<?> updateUser(@PathVariable("id") Long id, @ModelAttribute UpdateUserRequest request){
        try {
            UserResponse userResponse = userService.updateUser(id, request);
            return ResponseEntity.ok(new ApiResponse<>(200, userResponse));
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(404, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, "Failed to update user: " + e.getMessage()));
        }
    }


    // delete
    @DeleteMapping("/delete/{Id}")
    public ResponseEntity<?> deleteUser(@PathVariable("Id") Long Id) {
        try {
            userService.deleteUser(Id);
            return ResponseEntity
                    .ok(new ApiResponse<>(HttpStatus.OK.value(), "User deleted successfully"));
        } catch (DataNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(404, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to delete user: "+ e.getMessage()));
        }
    }

}
