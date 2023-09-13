package com.example.authservice.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/*
    @author: Dinh Quang Anh
    Date   : 8/9/2023
    Project: ExamApi
*/
@Entity
@Table(name = "verify_user_token")
@Data
public class EmailVerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime expiryDate;

    public EmailVerificationToken() {
        this.expiryDate = calculateExpiryDate();
    }

    public EmailVerificationToken(String token, User user) {
        this.token = token;
        this.user = user;
        this.expiryDate = calculateExpiryDate();
    }

    private LocalDateTime calculateExpiryDate() {
        return LocalDateTime.now().plusHours(24); // Token hết hiệu lực sau 24 giờ
    }
}

