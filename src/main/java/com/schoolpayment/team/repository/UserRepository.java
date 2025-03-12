package com.schoolpayment.team.repository;

import com.schoolpayment.team.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String username);
    Optional<User> findByname(String username);

    Optional<Object> findByName(String username);
}

