package com.schoolpayment.team.service;

import com.schoolpayment.team.dto.request.LoginRequest;
import com.schoolpayment.team.dto.request.UpdateUserRequest;
import com.schoolpayment.team.dto.request.UserRequest;
import com.schoolpayment.team.dto.response.UserResponse;
import com.schoolpayment.team.exception.DataNotFoundException;
import com.schoolpayment.team.exception.DuplicateDataException;
import com.schoolpayment.team.exception.InvalidPasswordException;
import com.schoolpayment.team.exception.UserNotFoundException;
import com.schoolpayment.team.model.User;
import com.schoolpayment.team.repository.StudentRepository;
import com.schoolpayment.team.repository.UserRepository;
import com.schoolpayment.team.security.CustomUserDetails;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    @Value("${file.IMAGE_DIR}")
    private String imageDirectory;

    private static final long maxFileSize = 5 * 1024 * 1024; // 5MB

    private final String[] allowedImageTypes = {"image/jpeg", "image/png", "image/jpg"};

    public String saveImageFile(MultipartFile file, String name) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("File is empty or not provided");
        }

        if (file.getSize() > maxFileSize) {
            throw new RuntimeException("File size exceeds the limit of " + maxFileSize + " bytes");
        }

        String fileType = file.getContentType();
        boolean isValidType = false;
        for (String allowedType : allowedImageTypes) {
            assert fileType != null;
            if (fileType.equals(allowedType)) {
                isValidType = true;
                break;
            }
        }

        if (!isValidType) {
            throw new RuntimeException("Invalid file type. Allowed types: " + String.join(", ", allowedImageTypes));
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String originalFileName = file.getOriginalFilename();
        String fileExtension = "";
        if (originalFileName != null && originalFileName.contains(".")) {
            fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }
        String customFileName = name + "_" + timeStamp + fileExtension;
        //String customFileName = name + "_" + timeStamp + "_" + originalFileName;

        Path path = Path.of(imageDirectory, customFileName);
        Files.copy(file.getInputStream(), path);

        return customFileName;
    }

    public static String toSlug(String input) {
        return input.toLowerCase()
                .replaceAll("[^a-z0-9\\s]", "")
                .replaceAll("\\s+", "-")
                .trim();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(()-> new UsernameNotFoundException("User not found with username: "+ username));
        return new CustomUserDetails(user);
    }

    // register
    @Transactional
    public UserResponse registerUser(UserRequest userRequest){
        if (studentRepository.existsByNis(userRequest.getNis())) {
            throw new DuplicateDataException("NIS already registered: " + userRequest.getNis());
        }
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

    // get all
    public Page<UserResponse> findAll(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<User> users = userRepository.findAllByOrderByUpdatedAtDesc(pageable);
            return users.map(this::convertToResponse);
        } catch (Exception e) {
            throw new RuntimeException("Failed to find all users: " + e.getMessage(), e);
        }
    }

    // get by id
    public UserResponse findUserById(Long id) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new DataNotFoundException("User with ID " + id + " not found"));
            return convertToResponse(user);
        } catch (DataNotFoundException e){
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to find user by id: " + e.getMessage(), e);
        }
    }


    // get By email
    public UserResponse findUserByEmail(String email) {
        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new DataNotFoundException("User with email " + email + " not found"));
            return convertToResponse(user);
        } catch (DataNotFoundException e){
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to find user by email: " + e.getMessage(), e);
        }
    }

    // filter
    public Page<UserResponse> filterUsersByRole(String role, int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<User> users = userRepository.findAllByRoleOrderByUpdatedAtDesc(role, pageable);
            return users.map(this::convertToResponse);
        } catch (Exception e) {
            throw new RuntimeException("Failed to find all users: " + e.getMessage(), e);
        }
    }

    //update
    @Transactional
    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        try{
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new DataNotFoundException("User with ID " + id + " not found"));

            if (request.getNis() != null) user.setNis(request.getNis());
            if (request.getEmail() != null) user.setEmail(request.getEmail());
            if (request.getName() != null) user.setName(request.getName());
            if (request.getPassword() != null) {
                user.setPassword(passwordEncoder.encode(request.getPassword()));
            }
            if (request.getRole() != null) user.setRole(request.getRole());

            if (request.getImage() != null && !request.getImage().isEmpty()) {
                String imagePath = saveImageFile(request.getImage(), toSlug(id.toString()));
                user.setImage(imagePath);
            }

            User updatedUser = userRepository.save(user);
            return convertToResponse(updatedUser);
        } catch (DataNotFoundException e){
            throw e;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    // delete
    @Transactional
    public void deleteUser(Long Id) {
        try {
            if(!userRepository.existsById(Id)) {
                throw new DataNotFoundException("User with id " + Id + " not found");
            }
            userRepository.deleteById(Id);
        } catch (DataNotFoundException e){
            throw e;
        }catch (Exception e) {
            throw new RuntimeException("Failed to delete username" + e.getMessage(), e);
        }
    }


    // convert to response
    private UserResponse convertToResponse(User user){
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setNis(user.getNis());
        userResponse.setName(user.getName());
        userResponse.setEmail(user.getEmail());
        userResponse.setRole(user.getRole());
        userResponse.setImage(user.getImage());
        userResponse.setCreatedAt(user.getCreatedAt());
        userResponse.setUpdatedAt(user.getUpdatedAt());
        return userResponse;
    }


}
