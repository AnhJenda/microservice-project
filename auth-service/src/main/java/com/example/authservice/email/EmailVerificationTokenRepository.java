package com.example.authservice.email;

import com.example.authservice.entity.EmailVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
    @author: Dinh Quang Anh
    Date   : 8/10/2023
    Project: ExamApi
*/
@Repository
public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, Long> {

    EmailVerificationToken findByToken(String token);
}
