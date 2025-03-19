package com.PicPaySimplificado.services.transaction;

import java.time.LocalDateTime;

import org.springframework.dao.CannotAcquireLockException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.PicPaySimplificado.domain.entities.AccountType;
import com.PicPaySimplificado.domain.entities.Transfer;
import com.PicPaySimplificado.domain.entities.User;
import com.PicPaySimplificado.domain.repositories.TransferRepository;
import com.PicPaySimplificado.dtos.email.EmailDto;
import com.PicPaySimplificado.infra.client.AuthorizeClient;
import com.PicPaySimplificado.infra.exceptions.NotAuthorizedException;
import com.PicPaySimplificado.services.email.EmailService;
import com.PicPaySimplificado.services.user.UserService;

import feign.FeignException;

@Service
public class TransactionService {

    private final TransferRepository transferRepository;
    private final AuthorizeClient authorizeClient;
    private final EmailService emailService;
    private final UserService userService;

    public TransactionService(TransferRepository transferRepository, AuthorizeClient authorizeClient,
            EmailService emailService, UserService userService) {
        this.transferRepository = transferRepository;
        this.authorizeClient = authorizeClient;
        this.emailService = emailService;
        this.userService = userService;
    }

    @Transactional(rollbackFor = { NotAuthorizedException.class,
            CannotAcquireLockException.class }, isolation = Isolation.SERIALIZABLE)
    public void executeTransfer(Long senderId, Long receiverId, Double amount) {

        // todo - Altera isso para capturar Exceptions alem do FeignException.Forbidden
        try {
            // AuthorizeResponseDto authorized = authorizeClient.authorize();
            authorizeClient.authorize();
        } catch (FeignException.Forbidden e) {
            throw new NotAuthorizedException("Unauthorized transfer");
        }

        User sender = userService.findByIdWithLock(senderId);

        // Sender validations
        // Verify if sender if PF
        if (sender.getType().equals(AccountType.PJ)) {
            throw new NotAuthorizedException("Only accounts of type PF, can send Transfers");
        }

        // verify if sender balance is < than amount
        if (sender.getBalance() < amount) {
            throw new NotAuthorizedException("Insufficient funds");
        }

        // Receiver Validations
        User receiver = userService.findByIdWithLock(receiverId);

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

    }

}
