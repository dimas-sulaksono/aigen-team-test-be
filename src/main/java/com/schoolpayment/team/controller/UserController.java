package com.schoolpayment.team.controller;

import com.schoolpayment.team.dto.request.LoginRequest;
import com.schoolpayment.team.dto.request.UserRequest;
import com.schoolpayment.team.dto.response.ApiResponse;
import com.schoolpayment.team.dto.response.UserResponse;
import com.schoolpayment.team.exception.DuplicateDataException;
import com.schoolpayment.team.repository.UserRepository;
import com.schoolpayment.team.service.UserService;
import com.schoolpayment.team.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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
}
