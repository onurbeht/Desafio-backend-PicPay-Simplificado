package com.PicPaySimplificado.services.transaction;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.PicPaySimplificado.domain.entities.Transfer;
import com.PicPaySimplificado.domain.entities.User;
import com.PicPaySimplificado.domain.repositories.TransferRepository;
import com.PicPaySimplificado.dtos.authorizeClient.AuthorizeResponseDto;
import com.PicPaySimplificado.infra.client.AuthorizeClient;
import com.PicPaySimplificado.infra.exceptions.NotAuthorizedException;

import feign.FeignException;

@Service
public class TransactionService {

    private final TransferRepository transferRepository;
    private final AuthorizeClient authorizeClient;

    public TransactionService(TransferRepository transferRepository, AuthorizeClient authorizeClient) {
        this.transferRepository = transferRepository;
        this.authorizeClient = authorizeClient;
    }

    public void executeTransfer(User sender, User receiver, Double amount) {

        //todo - Altera isso para capturar Exceptions alem do FeignException.Forbidden
        try {
            AuthorizeResponseDto authorized = authorizeClient.authorize();
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

        return;
    }

}
