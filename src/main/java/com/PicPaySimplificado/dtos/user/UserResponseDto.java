package com.PicPaySimplificado.dtos.user;

public record UserResponseDto(
        Long id,
        String firstName,
        String lastName,
        String email,
        String document,
        Double balance,
        String type) {

}
