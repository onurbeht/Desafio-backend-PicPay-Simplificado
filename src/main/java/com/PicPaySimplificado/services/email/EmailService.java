package com.PicPaySimplificado.services.email;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.PicPaySimplificado.dtos.email.EmailDto;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void send(EmailDto emailDto) {
        var email = new SimpleMailMessage();

        email.setFrom("noreply@test.com");
        email.setTo(emailDto.to());
        email.setSubject(emailDto.subject());
        email.setText(emailDto.body());

        mailSender.send(email);
    }

}
