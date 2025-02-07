package com.PicPaySimplificado.services.transaction;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.PicPaySimplificado.domain.entities.Transfer;
import com.PicPaySimplificado.domain.entities.User;
import com.PicPaySimplificado.domain.repositories.TransferRepository;
import com.PicPaySimplificado.dtos.email.EmailDto;
import com.PicPaySimplificado.infra.client.AuthorizeClient;
import com.PicPaySimplificado.infra.exceptions.NotAuthorizedException;
import com.PicPaySimplificado.services.email.EmailService;

import feign.FeignException;

@Service
public class TransactionService {

    private final TransferRepository transferRepository;
    private final AuthorizeClient authorizeClient;
    private final EmailService emailService;

    public TransactionService(TransferRepository transferRepository, AuthorizeClient authorizeClient,
            EmailService emailService) {
        this.transferRepository = transferRepository;
        this.authorizeClient = authorizeClient;
        this.emailService = emailService;
    }

    public void executeTransfer(User sender, User receiver, Double amount) {

        // todo - Altera isso para capturar Exceptions alem do FeignException.Forbidden
        try {
            // AuthorizeResponseDto authorized = authorizeClient.authorize();
            authorizeClient.authorize();
        } catch (FeignException.Forbidden e) {
            throw new NotAuthorizedException("Unauthorized transfer");
        }

        // update sender balance
        sender.setBalance(sender.getBalance() - amount);

        // update receiver balance
        receiver.setBalance(receiver.getBalance() + amount);

        // create entity Transfer and save
        transferRepository.save(
                Transfer.builder()
                        .senderId(sender.getId())
                        .receiverId(receiver.getId())
                        .amout(amount)
                        .timestamp(LocalDateTime.now())
                        .build());

        emailService.send(new EmailDto(receiver.getEmail(), "Payment received",
                "You received a payment of " + amount + " from " + sender.getFirstName() + " " + sender.getLastName()));

        return;
    }

}
