package com.schoolpayment.team.service;

import com.schoolpayment.team.dto.request.LoginRequest;
import com.schoolpayment.team.dto.request.UserRequest;
import com.schoolpayment.team.dto.response.UserResponse;
import com.schoolpayment.team.exception.DataNotFoundException;
import com.schoolpayment.team.exception.DuplicateDataException;
import com.schoolpayment.team.exception.InvalidPasswordException;
import com.schoolpayment.team.exception.UserNotFoundException;
import com.schoolpayment.team.model.User;
import com.schoolpayment.team.repository.UserRepository;
import com.schoolpayment.team.security.CustomUserDetails;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {


    private static final String IMAGE_DIRECTORY = "src/main/resources/static/images/";
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final String[] ALLOWED_FILE_TYPES = {"image/jpeg", "image/png", "image/jpg"};

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

    public List<UserResponse> findAll(){
        List<User> users = userRepository.findAll();
        return users.stream().map(this::convertToResponse).toList();
    }

    public UserResponse findById(Long id){
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        return convertToResponse(user);
    }

    public UserResponse findByUserName(String username){
        User user = userRepository.findByname(username)
                .orElseThrow(() -> new DataNotFoundException("User not found with username " + username));
        return convertToResponse(user);
    }

    @Transactional
    public UserResponse updateUser(UserRequest userRequest, String username, MultipartFile image) throws IOException {
        User user = (User) userRepository.findByName(username)
                .orElseThrow(() -> new DataNotFoundException("User not found with username " + username));

        if (userRequest.getName() != null) {
            user.setName(userRequest.getName());
        }
        if (userRequest.getNis() != null) {
            user.setNis(userRequest.getNis());
        }
        if (userRequest.getEmail() != null) {
            user.setEmail(userRequest.getEmail());
        }
        if (userRequest.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        }
        if (userRequest.getRole() != null) {
            user.setRole(userRequest.getRole());
        }
        if (image != null && !image.isEmpty()) {
            String savedFileName = saveImage(image);
            user.setImages(savedFileName);
        }

        User updatedUser = userRepository.save(user);
        return convertToResponse(updatedUser);
    }


    private String saveImage(MultipartFile images) throws IOException {
        if (images.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("Image size exceeds the maximum allowed size of " + MAX_FILE_SIZE / (1024 * 1024) + "MB");
        }

        String fileType = images.getContentType();
        boolean isValidType = false;

        for (String allowedType : ALLOWED_FILE_TYPES) {
            if (allowedType.equals(fileType)) {
                isValidType = true;
                break;
            }
        }

        if (!isValidType) {
            throw new IllegalArgumentException("Invalid file type. Only JPEG, PNG, and JPG files are allowed.");
        }

        String originalFilename = images.getOriginalFilename();
        String customFileName = System.currentTimeMillis() + "_" + originalFilename;

        Path path = Path.of(IMAGE_DIRECTORY + customFileName);
        Files.copy(images.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        return customFileName;
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
        userResponse.setImages(user.getImages());
        return userResponse;
    }

}
