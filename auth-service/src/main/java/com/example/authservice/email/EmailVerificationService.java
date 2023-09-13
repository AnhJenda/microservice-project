package com.example.authservice.email;

import com.example.authservice.entity.EmailVerificationToken;
import com.example.authservice.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.UUID;

/*
    @author: Dinh Quang Anh
    Date   : 8/9/2023
    Project: ExamApi
*/
@Service
public class EmailVerificationService {
    @Autowired
    private EmailVerificationTokenRepository emailVerificationTokenRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendVerificationEmail(User user) {
        // Tạo token xác nhận email
        String token = UUID.randomUUID().toString();
        EmailVerificationToken verificationToken = new EmailVerificationToken(token, user);
        emailVerificationTokenRepository.save(verificationToken);

        // Gửi email xác nhận
        String subject = "Verify Your Email Address";
        String body = "Please click the following link to verify your email address: "
                + "http://localhost:3000/api/v1/verify-email?token=" + token;
        sendEmail(user.getEmail(), subject, body);
    }

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        javaMailSender.send(message);
    }
}
