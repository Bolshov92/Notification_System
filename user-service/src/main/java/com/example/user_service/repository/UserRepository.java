package com.example.user_service.repository;


import com.example.user_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserInfoUserName(String userName);

    User findUserById(Long id);

}