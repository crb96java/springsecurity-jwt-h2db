package com.springsecurity.jwt.repository;

import com.springsecurity.jwt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    User findByUserName(String username);
}
