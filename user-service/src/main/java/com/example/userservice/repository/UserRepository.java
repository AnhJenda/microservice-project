package com.example.userservice.repository;

import com.example.userservice.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/*
    @author: Dinh Quang Anh
    Date   : 9/6/2023
    Project: user-service
*/
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User getUserById(long id);
    Optional<User> getUserByEmail(String email);
    User findUserByEmail(String email);
    boolean existsByEmail(String username);
    List<User> findAll();
}
