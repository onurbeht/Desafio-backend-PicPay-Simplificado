package com.PicPaySimplificado.controllers;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.PicPaySimplificado.domain.entities.AccountType;
import com.PicPaySimplificado.domain.entities.User;
import com.PicPaySimplificado.dtos.transfer.TransferRequestDto;
import com.PicPaySimplificado.dtos.transfer.TransferResponseDto;
import com.PicPaySimplificado.infra.exceptions.NotAuthorizedException;
import com.PicPaySimplificado.services.transaction.TransactionService;
import com.PicPaySimplificado.services.user.UserService;

@RestController
@RequestMapping("/transfer")
public class TransactionController {

    private final TransactionService transactionService;
    private final UserService userService;

    public TransactionController(TransactionService transactionService, UserService userService) {
        this.transactionService = transactionService;
        this.userService = userService;
    }

    // Todo - Implementar validações, nos valores que vem do DTO

    @PostMapping("/send")
    @Transactional(rollbackFor = NotAuthorizedException.class)
    public ResponseEntity<?> transfer(@RequestBody TransferRequestDto transferRequestDto) {

        // Sender Validations
        Optional<User> possibleSender = userService.findById(transferRequestDto.senderId());

        if (possibleSender.isEmpty()) {
            return ResponseEntity.badRequest().body("SenderId Invalid");
        }

        // Verify if sender if PF
        if (!possibleSender.get().getType().equals(AccountType.PF)) {
            return ResponseEntity.badRequest().body("Only accounts of type PF, can send Transfers");
        }

        // verify if sender balance is > than amount
        if (possibleSender.get().getBalance() < transferRequestDto.amount()) {
            return ResponseEntity.badRequest().body("Insufficient funds");
        }

        // Receiver Validations
        Optional<User> possibleReceiver = userService.findById(transferRequestDto.receiverId());

        if (possibleReceiver.isEmpty()) {
            return ResponseEntity.badRequest().body("ReceiverId Invalid");
        }

        // Execute transfer
        transactionService.executeTransfer(possibleSender.get(), possibleReceiver.get(), transferRequestDto.amount());

        return ResponseEntity.ok(
                new TransferResponseDto("Amount transferred successfully to ID: " + transferRequestDto.receiverId()));
    }

}
