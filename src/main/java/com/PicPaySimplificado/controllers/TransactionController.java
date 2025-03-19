package com.PicPaySimplificado.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.PicPaySimplificado.dtos.transfer.TransferRequestDto;
import com.PicPaySimplificado.dtos.transfer.TransferResponseDto;
import com.PicPaySimplificado.services.transaction.TransactionService;

@RestController
@RequestMapping("/transfer")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    // Todo - Implementar validações, nos valores que vem do DTO

    @PostMapping("/send")
    public ResponseEntity<?> transfer(@RequestBody TransferRequestDto transferRequestDto) {
        // check if senderId == receiverId
        if (transferRequestDto.senderId().equals(transferRequestDto.receiverId())) {
            return ResponseEntity.badRequest().body("SenderID cannot be equals to ReceiverID");
        }

        // Execute transfer
        transactionService.executeTransfer(transferRequestDto.senderId(), transferRequestDto.receiverId(),
                transferRequestDto.amount());

        return ResponseEntity.ok(
                new TransferResponseDto("Amount transferred successfully to ID: " + transferRequestDto.receiverId()));
    }

}
