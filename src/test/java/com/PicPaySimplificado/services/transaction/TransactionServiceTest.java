package com.PicPaySimplificado.services.transaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.PicPaySimplificado.domain.entities.AccountType;
import com.PicPaySimplificado.domain.entities.Transfer;
import com.PicPaySimplificado.domain.entities.User;
import com.PicPaySimplificado.domain.repositories.TransferRepository;
import com.PicPaySimplificado.dtos.email.EmailDto;
import com.PicPaySimplificado.infra.client.AuthorizeClient;
import com.PicPaySimplificado.infra.exceptions.NotAuthorizedException;
import com.PicPaySimplificado.services.email.EmailService;

import feign.FeignException;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    TransferRepository transferRepository;

    @Mock
    AuthorizeClient authorizeClient;

    @Mock
    EmailService emailService;

    @InjectMocks
    TransactionService transactionService;

    User sender;
    User receiver;

    @BeforeEach
    void setUp() {
        sender = User.builder().id(1L).firstName("sender").lastName("senderLast").email("emailSender").document("CPF")
                .balance(200.0).type(AccountType.PF).build();

        receiver = User.builder().id(2L).firstName("receiver").lastName("receiverLast").email("emailReceiver")
                .document("CNPJ").balance(100.0).type(AccountType.PJ).build();
    }

    @Test
    void shouldSuccessfullyExecuteTransfer() {
        // Arrange: Prepare the necessary data and mock behaviors
        Double amount = 50.0;

        EmailDto emailDto = new EmailDto(receiver.getEmail(), "Payment received",
                "You received a payment of " + amount + " from " + sender.getFirstName() + " " + sender.getLastName());

        // Mocking the authorizeClient to simulate a successful authorization
        when(authorizeClient.authorize()).thenReturn(null);
        doNothing().when(emailService).send(emailDto);

        // Act: Execute the method being tested
        transactionService.executeTransfer(sender, receiver, amount);

        // Assert: Verify the expected results
        assertEquals(150.0, sender.getBalance(), "Sender's balance should be decreased by the transfer amount.");
        assertEquals(150.0, receiver.getBalance(), "Receiver's balance should be increased by the transfer amount.");

        // Verify that the transferRepository.save was called once
        verify(transferRepository, times(1)).save(any(Transfer.class));

        // Verify that the emailService.send was called once with the expected email
        // content
        verify(emailService, times(1)).send(emailDto);
    }

    @Test
    void shouldThrowNotAuthorizedExceptionWhenAuthorizationFails() {
        // Arrange: Prepare the necessary data and mock behaviors
        Double amount = 50.0;

        // Mocking the authorizeClient to simulate a forbidden authorization
        // (authorization failure)
        doThrow(FeignException.Forbidden.class).when(authorizeClient).authorize();

        // Act: Execute the method being tested
        assertThrows(NotAuthorizedException.class, () -> transactionService.executeTransfer(sender, receiver, amount));

        // Assert: Verify the expected results
        assertEquals(200.0, sender.getBalance(), "Sender's balance should not be decreased by the transfer amount.");
        assertEquals(100.0, receiver.getBalance(),
                "Receiver's balance should not be increased by the transfer amount.");

        // Verify that the transferRepository.save was called once
        verify(transferRepository, times(0)).save(any(Transfer.class));

        // Verify that the emailService.send was called once with the expected email
        // content
        verify(emailService, times(0)).send(any(EmailDto.class));
    }

}
