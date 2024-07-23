package com.emergency.repository;

import com.emergency.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserInfoUserName(String userName);

    User findUserById(Long id);
}