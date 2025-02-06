package com.PicPaySimplificado.dtos.transfer;

public record TransferRequestDto(
                Double amount,
                Long senderId,
                Long receiverId) {

}
