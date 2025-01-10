package com.example.outsourcingproject.domain.user.repository;

import com.example.outsourcingproject.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {

	User findByEmail(String email);

	boolean existsByEmail(String email);
}
