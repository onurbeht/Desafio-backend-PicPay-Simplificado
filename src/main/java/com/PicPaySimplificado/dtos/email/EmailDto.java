package com.PicPaySimplificado.dtos.email;

public record EmailDto(
        String to,
        String subject,
        String body) {

}
