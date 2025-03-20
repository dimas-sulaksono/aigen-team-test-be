package com.schoolpayment.team.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.schoolpayment.team.dto.request.LoginRequest;
import com.schoolpayment.team.dto.request.UpdateUserRequest;
import com.schoolpayment.team.dto.request.UserRequest;
import com.schoolpayment.team.dto.response.UserResponse;
import com.schoolpayment.team.exception.DataNotFoundException;
import com.schoolpayment.team.exception.DuplicateDataException;
import com.schoolpayment.team.exception.InvalidPasswordException;
import com.schoolpayment.team.exception.UserNotFoundException;
import com.schoolpayment.team.model.Student;
import com.schoolpayment.team.model.User;
import com.schoolpayment.team.repository.StudentRepository;
import com.schoolpayment.team.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private Student student;

    private User user;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setId(1L);
        student.setNis("1234567");
        student.setName("Test Student");
        student.setClassEntity(null);
        student.setAddress("Test Address");
        student.setPhoneNumber("1234567890");
        student.setBirthdate(null);

        user = new User();
        user.setId(1L);
        user.setNis("1234567");
        user.setEmail("testuser@gmail.com");
        user.setName("Test User");
        user.setPassword("password123");
    }

    @Test
    void testFindUserById_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        UserResponse response = userService.findUserById(1L);
        assertNotNull(response);
        assertEquals("testuser@gmail.com", response.getEmail());
    }

    @Test
    void testFindUserById_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(DataNotFoundException.class, () -> userService.findUserById(1L));
    }

    @Test
    void testCreateUser_Success() {
        UserRequest request = new UserRequest();
        request.setNis("1234568");
        request.setEmail("newuser2@gmail.com");
        request.setName("New User");
        request.setPassword("newpassword");

        when(userRepository.findByEmail("newuser2@gmail.com")).thenReturn(Optional.empty());
        when(studentRepository.existsByNis("1234568")).thenReturn(true);
        when(passwordEncoder.encode("newpassword")).thenReturn("hashedpassword");

        User newUser = new User();
        newUser.setId(2L);
        newUser.setNis("1234568");
        newUser.setEmail("newuser2@gmail.com");
        newUser.setName("New User");
        newUser.setPassword("hashedpassword");

        when(userRepository.save(any(User.class))).thenReturn(newUser); // ✅ Gunakan objek yang benar

        UserResponse response = userService.registerUser(request);
        assertNotNull(response);
        assertEquals("newuser2@gmail.com", response.getEmail()); // ✅ Harus sesuai dengan data yang dikembalikan
    }


    @Test
    void testCreateUser_DuplicateUsername() {
        UserRequest request = new UserRequest();
        request.setNis("1234567");
        request.setEmail("testuser@gmail.com");
        request.setName("Test User");
        request.setPassword("password123");

        when(studentRepository.existsByNis("1234567")).thenReturn(true);
        when(userRepository.findByEmail("testuser@gmail.com")).thenReturn(Optional.of(user));

        assertThrows(DuplicateDataException.class, () -> userService.registerUser(request));
    }

}
