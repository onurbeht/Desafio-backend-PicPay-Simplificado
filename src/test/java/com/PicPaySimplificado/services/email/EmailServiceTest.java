package com.PicPaySimplificado.services.email;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.PicPaySimplificado.dtos.email.EmailDto;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @Mock
    JavaMailSender javaMailSender;

    @InjectMocks
    EmailService emailService;

    EmailDto emailDto;

    @BeforeEach
    void setUp() {
        emailDto = new EmailDto("RECEIVER", "Payment received",
                "You received a payment of " + 10.0 + " from SENDER FIRST LAST NAME");
    }

    @Test
    void shouldSendEmailSuccessfully() {

        // Arrange: Prepare the necessary data and mock behaviors
        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom("noreply@test.com");
        email.setTo(emailDto.to());
        email.setSubject(emailDto.subject());
        email.setText(emailDto.body());

        // Act: Execute the method being tested
        emailService.send(emailDto);

        // Assert: Verify that the mailSender.send method was called with the expected
        // email
        verify(javaMailSender, times(1)).send(email);
    }

}
