package com.schoolpayment.team.service;

import com.schoolpayment.team.dto.request.LoginRequest;
import com.schoolpayment.team.dto.request.UserRequest;
import com.schoolpayment.team.dto.response.ApiResponse;
import com.schoolpayment.team.dto.response.UserResponse;
import com.schoolpayment.team.exception.DuplicateDataException;
import com.schoolpayment.team.exception.InvalidPasswordException;
import com.schoolpayment.team.exception.UserNotFoundException;
import com.schoolpayment.team.model.User;
import com.schoolpayment.team.repository.UserRepository;
import com.schoolpayment.team.security.CustomUserDetails;
import com.schoolpayment.team.util.JwtUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(()-> new UsernameNotFoundException("User not found with username: "+ username));
        return new CustomUserDetails(user);
    }

    // register
    @Transactional
    public UserResponse registerUser(UserRequest userRequest){
        Optional<User> existingUser = userRepository.findByEmail(userRequest.getEmail());
        if (existingUser.isPresent()) {
            throw new DuplicateDataException("User already exists with username: " + userRequest.getEmail());}

        Optional<User> existingEmail = userRepository.findByEmail(userRequest.getEmail());
        if (existingEmail.isPresent()) {
            throw new DuplicateDataException("Email already registered: " + userRequest.getEmail());}

        if (userRequest.getPassword().length() < 8) {
            throw new RuntimeException("Password must be at least 8 characters long");}

        User user = new User();
        user.setEmail(userRequest.getEmail());
        user.setNis(userRequest.getNis());
        user.setName(userRequest.getName());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setRole(Optional.ofNullable(userRequest.getRole()).orElse("CUSTOMER"));
        user = userRepository.save(user);
        User register = userRepository.save(user);
        return convertToResponse(register);
    }


    // login
    public UserResponse loginUser(LoginRequest loginRequest){
        Optional<User> userOptional = userRepository.findByEmail(loginRequest.getUsername());

        if (userOptional.isEmpty()){
            throw new UserNotFoundException("User not found with username: "+ loginRequest.getUsername());}

        User user = userOptional.get();

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new InvalidPasswordException("Invalid password");}
        return convertToResponse(user);
    }

    private UserResponse convertToResponse(User user){
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setNis(user.getNis());
        userResponse.setName(user.getName());
        userResponse.setEmail(user.getEmail());
        userResponse.setRole(user.getRole());
        userResponse.setCreatedAt(user.getCreatedAt());
        userResponse.setUpdatedAt(user.getUpdatedAt());
        return userResponse;
    }

}
